package g.sig.questweaver.game.home.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import g.sig.questweaver.common.ui.mappers.toColor
import g.sig.questweaver.domain.entities.blocks.Size
import g.sig.questweaver.domain.entities.common.Annotation
import g.sig.questweaver.domain.entities.common.Game
import g.sig.questweaver.domain.entities.common.RemoveAnnotation
import g.sig.questweaver.domain.entities.states.GameState
import g.sig.questweaver.domain.entities.states.RequestGameState
import g.sig.questweaver.domain.usecases.game.GetGameUseCase
import g.sig.questweaver.domain.usecases.game.UpdateGameUseCase
import g.sig.questweaver.domain.usecases.game.state.CreateOrUpdateGameStateUseCase
import g.sig.questweaver.domain.usecases.game.state.GetGameStateUseCase
import g.sig.questweaver.domain.usecases.game.state.UpdateGameStateUseCase
import g.sig.questweaver.domain.usecases.nearby.BroadcastPayloadUseCase
import g.sig.questweaver.domain.usecases.nearby.OnPayloadReceivedUseCase
import g.sig.questweaver.domain.usecases.nearby.SendPayloadUseCase
import g.sig.questweaver.domain.usecases.user.GetUserUseCase
import g.sig.questweaver.game.home.state.GameHomeEvent
import g.sig.questweaver.game.home.state.GameHomeIntent
import g.sig.questweaver.game.home.state.GameHomeState
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class GameHomeViewModel
    @Inject
    constructor(
        private val broadcastPayload: BroadcastPayloadUseCase,
        private val sendPayloadUseCase: SendPayloadUseCase,
        private val onPayloadReceived: OnPayloadReceivedUseCase,
        private val getGameState: GetGameStateUseCase,
        private val createOrUpdateGameStateUseCase: CreateOrUpdateGameStateUseCase,
        private val updateGameStateUseCase: UpdateGameStateUseCase,
        private val updateGameUseCase: UpdateGameUseCase,
        private val getGame: GetGameUseCase,
        private val getUser: GetUserUseCase,
    ) : ViewModel() {
        private val _events = Channel<GameHomeEvent>()
        val events = _events.receiveAsFlow()
        val state = GameHomeState()

        fun handleIntent(intent: GameHomeIntent) {
            when (intent) {
                is GameHomeIntent.Back -> viewModelScope.launch { _events.send(GameHomeEvent.Back) }
                is GameHomeIntent.Load -> loadHomeScreen()
                is GameHomeIntent.AddDrawing -> addDrawing(intent)
                is GameHomeIntent.AddText -> addText(intent)
                is GameHomeIntent.SelectAnnotation -> selectAnnotation(intent)
                is GameHomeIntent.SelectColor -> state.selectedColor = intent.color
                is GameHomeIntent.SelectSize -> state.selectedSize = Size(intent.size, intent.size)
                is GameHomeIntent.ChangeMode -> state.annotationMode = intent.mode
                is GameHomeIntent.SelectPlayer -> state.selectedPlayer = intent.player
                GameHomeIntent.ShowColorPicker -> state.showColorPicker = true

                is GameHomeIntent.SelectOpacity -> {
                    state.selectedColor = state.selectedColor.copy(alpha = intent.opacity)
                }

                is GameHomeIntent.AddImage -> {
                    // TODO Add image
                }
            }
        }

        private fun addDrawing(intent: GameHomeIntent.AddDrawing) =
            viewModelScope.launch {
                if (state.opacity < ALPHA_MIN) return@launch

                val points =
                    intent.path.runningReduce { acc, point ->
                        if (acc.distanceTo(point) > POINT_TOLERANCE) point else acc
                    }

                val drawing =
                    Annotation.Drawing(
                        path = points,
                        strokeSize = intent.strokeSize,
                        color = state.selectedColor.toColor(),
                        createdBy = getUser().id,
                        id = UUID.randomUUID().toString(),
                    )
                broadcastPayload(drawing)
                addAnnotation(drawing)
            }

        private fun addText(intent: GameHomeIntent.AddText) =
            viewModelScope.launch {
                val text =
                    Annotation.Text(
                        text = intent.text,
                        size = intent.size,
                        color = state.selectedColor.toColor(),
                        anchor = intent.anchor,
                        createdBy = getUser().id,
                        id = UUID.randomUUID().toString(),
                    )
                broadcastPayload(text)
                addAnnotation(text)
            }

        private var loadingJob: Job? = null
            set(value) {
                field?.cancel()
                field = value
            }

        private fun loadHomeScreen() =
            viewModelScope
                .launch {
                    updateGameState()
                    onPayloadReceived {
                        when (val data = it.payloadData.data) {
                            is Annotation -> addAnnotation(data)

                            is RemoveAnnotation -> removeAnnotation(data.id)

                            is GameState -> {
                                createOrUpdateGameStateUseCase(data)
                                updateGameState(data)
                            }

                            is Game -> updateGameUseCase(data)

                            else -> Unit
                        }
                    }
                }.also { loadingJob = it }

        private fun selectAnnotation(intent: GameHomeIntent.SelectAnnotation) =
            viewModelScope.launch {
                when (state.annotationMode) {
                    GameHomeState.AnnotationMode.RemoveMode -> attemptRemoveAnnotation(intent.annotation?.id)

                    else -> state.selectedAnnotation = intent.annotation
                }
            }

        private fun attemptRemoveAnnotation(annotationId: String?) =
            viewModelScope.launch {
                val annotation = state.annotations[annotationId] ?: return@launch
                val user = getUser()
                val canRemoveAnnotation = state.isDM || annotation.createdBy == user.id
                if (canRemoveAnnotation) {
                    val payload = RemoveAnnotation(annotation.id)
                    broadcastPayload(payload)
                    removeAnnotation(annotation.id)
                }
            }

        private suspend fun updateGameState(gameStateReceived: GameState? = null) {
            val game = getGame()
            val user = getUser()
            val isDM = game?.dmId == user.id
            val gameState =
                when {
                    isDM -> checkNotNull(getGameState())
                    gameStateReceived != null -> gameStateReceived
                    else -> {
                        game?.hostDeviceId?.let { sendPayloadUseCase(it, RequestGameState) }
                        return
                    }
                }

            state.isDM = isDM
            state.users = gameState.connectedUsers
            state.annotations = gameState.annotations.associateBy { it.id }
            state.allowAnnotations = gameState.allowEditing || isDM
        }

        private fun addAnnotation(annotation: Annotation) {
        val updatedAnnotations = state.annotations.toMutableMap()
            updatedAnnotations[annotation.id] = annotation
            state.annotations = updatedAnnotations
        viewModelScope.launch {
            updateGameStateUseCase(annotations = updatedAnnotations.values.toList())
        }
        }

    private fun removeAnnotation(annotationId: String) {
        val updatedAnnotations = state.annotations.toMutableMap()
        updatedAnnotations.remove(annotationId)
        state.annotations = updatedAnnotations
        viewModelScope.launch {
            updateGameStateUseCase(annotations = updatedAnnotations.values.toList())
        }
    }

    companion object {
        private const val ALPHA_MIN = 0.1f
        private const val POINT_TOLERANCE = 0.01f
    }
}
