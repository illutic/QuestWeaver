package g.sig.questweaver.game.home.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import g.sig.questweaver.common.ui.mappers.toColor
import g.sig.questweaver.common.ui.mappers.topLeft
import g.sig.questweaver.domain.entities.blocks.Size
import g.sig.questweaver.domain.entities.common.Annotation
import g.sig.questweaver.domain.entities.common.Game
import g.sig.questweaver.domain.entities.common.RemoveAnnotation
import g.sig.questweaver.domain.entities.common.TransformationData
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

// TODO - Move logic to domain

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
        private val _state = MutableStateFlow<GameHomeState>(GameHomeState.Loading)
        private val _events = Channel<GameHomeEvent>()
        val events = _events.receiveAsFlow()
        val state = _state.asStateFlow()

        fun handleIntent(intent: GameHomeIntent) {
            when (intent) {
                is GameHomeIntent.Back -> viewModelScope.launch { _events.send(GameHomeEvent.Back) }
                is GameHomeIntent.Load -> loadHomeScreen()
                is GameHomeIntent.AddDrawing -> addDrawing(intent)
                is GameHomeIntent.AddText -> addText(intent)
                is GameHomeIntent.SelectAnnotation -> selectAnnotation(intent)
                is GameHomeIntent.SelectColor -> setState { copy(selectedColor = intent.color) }
                is GameHomeIntent.SelectSize ->
                    setState { copy(selectedSize = Size(intent.size, intent.size)) }

                is GameHomeIntent.ChangeMode -> setState { copy(annotationMode = intent.mode) }
                is GameHomeIntent.SelectPlayer -> setState { copy(selectedPlayer = intent.player) }
                is GameHomeIntent.ChangeText -> updateText(intent)
                GameHomeIntent.ShowColorPicker -> setState { copy(showColorPicker = true) }
                GameHomeIntent.HideColorPicker -> setState { copy(showColorPicker = false) }

                is GameHomeIntent.SelectOpacity -> {
                    setState { copy(selectedColor = selectedColor.copy(alpha = intent.opacity)) }
                }

                is GameHomeIntent.AddImage -> {
                    // TODO Add image
                }

                is GameHomeIntent.CommitTransformation -> {
                    updateAnnotationTransformationData(
                        intent.id,
                        TransformationData(
                            scale = intent.scale,
                            rotation = intent.rotation,
                            anchor = intent.anchor,
                        ),
                    )
                }
            }
        }

        private fun addDrawing(intent: GameHomeIntent.AddDrawing) =
            viewModelScope.launch {
                val state = state.value
                if (state !is GameHomeState.Loaded) return@launch

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
                        createdBy = state.currentUser.id,
                        id = UUID.randomUUID().toString(),
                        transformationData =
                            TransformationData(
                                scale = 1f,
                                rotation = 0f,
                                anchor = points.topLeft(),
                            ),
                    )
                broadcastPayload(drawing)
                addAnnotation(drawing)
            }

        private fun addText(intent: GameHomeIntent.AddText) =
            viewModelScope.launch {
                val state = state.value
                if (state !is GameHomeState.Loaded) return@launch

                val text =
                    Annotation.Text(
                        text = "",
                        color = state.selectedColor.toColor(),
                        createdBy = state.currentUser.id,
                        id = UUID.randomUUID().toString(),
                        transformationData = intent.transformationData,
                    )
                addAnnotation(text)
            }

        private fun updateText(intent: GameHomeIntent.ChangeText) {
            val state = state.value
            if (state !is GameHomeState.Loaded) return

            val textAnnotation = state.annotations[intent.id] as? Annotation.Text ?: return
            val updatedText = textAnnotation.copy(text = intent.text)

            if (updatedText.text.isEmpty()) {
                attemptRemoveAnnotation(updatedText.id)
            } else {
                updateAnnotation(updatedText)
            }
        }

        private var loadingJob: Job? = null
            set(value) {
                field?.cancel()
                field = value
            }

        private fun loadHomeScreen() =
            viewModelScope
                .launch {
                    _state.value = GameHomeState.Loaded(currentUser = getUser())
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
                val state = state.value
                if (state !is GameHomeState.Loaded) return@launch

                when (state.annotationMode) {
                    GameHomeState.AnnotationMode.RemoveMode -> attemptRemoveAnnotation(intent.annotation?.id)

                    else -> setState { copy(selectedAnnotation = intent.annotation) }
                }
            }

        private fun attemptRemoveAnnotation(annotationId: String?) =
            viewModelScope.launch {
                val state = state.value
                if (state !is GameHomeState.Loaded) return@launch

                val user = state.currentUser
                val annotation = state.annotations[annotationId] ?: return@launch
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

            setState {
                copy(
                    isDM = isDM,
                    currentUser = user,
                    users = gameState.connectedUsers,
                    annotations = gameState.annotations.associateBy { it.id },
                    allowAnnotations = gameState.allowEditing || isDM,
                )
            }
        }

        private fun addAnnotation(annotation: Annotation) {
            viewModelScope.launch {
                val state = state.value
                if (state !is GameHomeState.Loaded) return@launch

                val updatedAnnotations = state.annotations.toMutableMap()
                updatedAnnotations[annotation.id] = annotation
                setState { copy(annotations = updatedAnnotations) }
                updateGameStateUseCase(
                    annotations = updatedAnnotations.values.toList(),
                )
            }
        }

        private fun updateAnnotation(annotation: Annotation) {
            viewModelScope.launch {
                addAnnotation(annotation)
                broadcastPayload(annotation)
            }
        }

        private fun updateAnnotationTransformationData(
            id: String,
            data: TransformationData,
        ) {
            val state = state.value
            if (state !is GameHomeState.Loaded) return

            val updatedAnnotations = state.annotations.toMutableMap()
            val annotation = updatedAnnotations[id] ?: return
            val updatedAnnotation =
                when (annotation) {
                    is Annotation.Text -> annotation.copy(transformationData = data)
                    is Annotation.Drawing -> annotation.copy(transformationData = data)
                    is Annotation.Image -> annotation.copy(transformationData = data)
                }
            updatedAnnotations[id] = updatedAnnotation
            setState { copy(annotations = updatedAnnotations) }
            viewModelScope.launch {
                broadcastPayload(updatedAnnotation)
                updateGameStateUseCase(annotations = updatedAnnotations.values.toList())
            }
        }

        private fun removeAnnotation(annotationId: String) {
            val state = state.value
            if (state !is GameHomeState.Loaded) return

            val updatedAnnotations = state.annotations.toMutableMap()
            updatedAnnotations.remove(annotationId)
            setState { copy(annotations = updatedAnnotations) }
            viewModelScope.launch {
                updateGameStateUseCase(annotations = updatedAnnotations.values.toList())
            }
        }

        private inline fun setState(block: GameHomeState.Loaded.() -> GameHomeState) =
            _state.update {
                if (it is GameHomeState.Loaded) it.block() else it
            }

        companion object {
            private const val ALPHA_MIN = 0.1f
            private const val POINT_TOLERANCE = 0.01f
        }
    }
