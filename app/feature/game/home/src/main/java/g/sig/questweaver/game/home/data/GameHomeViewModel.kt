package g.sig.questweaver.game.home.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import g.sig.questweaver.domain.entities.blocks.Point
import g.sig.questweaver.domain.entities.common.Annotation
import g.sig.questweaver.domain.entities.common.RemoveAnnotation
import g.sig.questweaver.domain.entities.states.GameState
import g.sig.questweaver.domain.usecases.game.GetGameUseCase
import g.sig.questweaver.domain.usecases.game.state.LoadGameStateUseCase
import g.sig.questweaver.domain.usecases.game.state.LoadInitialGameStateUseCase
import g.sig.questweaver.domain.usecases.nearby.BroadcastPayloadUseCase
import g.sig.questweaver.domain.usecases.user.GetUserUseCase
import g.sig.questweaver.game.home.state.AnnotationMode
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
import javax.inject.Inject

@HiltViewModel
class GameHomeViewModel
    @Inject
    constructor(
        private val loadGameState: LoadGameStateUseCase,
        private val loadInitialGameStateUseCase: LoadInitialGameStateUseCase,
        private val getGame: GetGameUseCase,
        private val getUser: GetUserUseCase,
        private val broadcastPayload: BroadcastPayloadUseCase,
    ) : ViewModel() {
        private val _state = MutableStateFlow(GameHomeState())
        private val _events = Channel<GameHomeEvent>()
        val events = _events.receiveAsFlow()
        val state = _state.asStateFlow()

        fun handleIntent(intent: GameHomeIntent) {
            when (intent) {
                is GameHomeIntent.Back -> viewModelScope.launch { _events.send(GameHomeEvent.Back) }
                is GameHomeIntent.Load -> loadHomeScreen()
                is GameHomeIntent.AddDrawing -> addDrawing(intent.path)
                is GameHomeIntent.AddText -> addText(intent.anchor)
                is GameHomeIntent.SelectAnnotation -> selectAnnotation(intent.annotation)
                is GameHomeIntent.SelectSize -> setState { updateSize(intent.size) }
                is GameHomeIntent.SelectOpacity -> setState { updateOpacity(intent.opacity) }
                is GameHomeIntent.ChangeText -> updateText(intent.id, intent.text)
                is GameHomeIntent.SelectColor -> setState { copy(selectedColor = intent.color) }
                is GameHomeIntent.SelectPlayer -> setState { copy(selectedPlayer = intent.player) }
                is GameHomeIntent.ChangeMode -> setState { copy(annotationMode = intent.mode) }
                is GameHomeIntent.AddImage -> {}
                GameHomeIntent.ShowColorPicker -> setState { copy(showColorPicker = true) }
                GameHomeIntent.HideColorPicker -> setState { copy(showColorPicker = false) }

                is GameHomeIntent.CommitTransformation ->
                    updateTransformation(
                        id = intent.id,
                        scale = intent.scale,
                        rotation = intent.rotation,
                        anchor = intent.anchor,
                    )
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
                    launch { loadGameState { updateGameState(it) } }
                    loadInitialGameStateUseCase()
                }.also {
                    loadingJob = it
                }

        private suspend fun updateGameState(gameState: GameState) {
            val game = getGame(gameState.gameId)
            val user = getUser()
            val isDM = game.dmId == user.id

            setState {
                copy(
                    isDM = isDM,
                    users = gameState.connectedUsers,
                    annotations = gameState.annotations.associateBy { it.id },
                    allowAnnotations = gameState.allowEditing || isDM,
                )
            }
        }

        private fun addDrawing(path: List<Point>) {
            val annotation = state.value.makeDrawing(path)
            if (annotation != null) {
                setState { copy(annotations = annotations + (annotation.id to annotation)) }
                viewModelScope.launch {
                    broadcastPayload(annotation)
                }
            }
        }

        private fun addText(anchor: Point) {
            val annotation = state.value.makeText(anchor)
            if (annotation != null) {
                setState { copy(annotations = annotations + (annotation.id to annotation)) }
                viewModelScope.launch {
                    broadcastPayload(annotation)
                }
            }
        }

        private fun updateText(
            id: String,
            text: String,
        ) {
            val annotation = state.value.getUpdatedText(id, text)
            if (annotation != null) {
                when (annotation) {
                    is RemoveAnnotation -> {
                        setState { copy(annotations = annotations - annotation.id) }
                    }
                    is Annotation.Text -> {
                        setState { copy(annotations = annotations + (annotation.id to annotation)) }
                    }
                }
                viewModelScope.launch {
                    broadcastPayload(annotation)
                }
            }
        }

        private fun selectAnnotation(annotation: Annotation) {
            val state = state.value
            val selectedAnnotation = state.annotations[annotation.id] ?: return
            if (state.annotationMode == AnnotationMode.RemoveMode) {
                setState { copy(annotations = state.annotations - annotation.id) }
                viewModelScope.launch {
                    broadcastPayload(RemoveAnnotation(selectedAnnotation.id))
                }
            }
        }

        private fun updateTransformation(
            id: String,
            scale: Float,
            rotation: Float,
            anchor: Point,
        ) {
            val annotation = state.value.updateTransformation(id, scale, rotation, anchor)
            if (annotation != null) {
                setState { copy(annotations = annotations + (annotation.id to annotation)) }
                viewModelScope.launch {
                    broadcastPayload(annotation)
                }
            }
        }

        private inline fun setState(block: GameHomeState.() -> GameHomeState) = _state.update(block)
    }
