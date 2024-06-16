package g.sig.questweaver.game.home.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import g.sig.questweaver.common.ui.mappers.toColor
import g.sig.questweaver.common.ui.mappers.toComposeColor
import g.sig.questweaver.domain.entities.common.Annotation
import g.sig.questweaver.domain.usecases.game.GetGameStateUseCase
import g.sig.questweaver.domain.usecases.nearby.BroadcastPayloadUseCase
import g.sig.questweaver.domain.usecases.user.GetUserUseCase
import g.sig.questweaver.game.home.state.GameHomeEvent
import g.sig.questweaver.game.home.state.GameHomeIntent
import g.sig.questweaver.game.home.state.GameHomeState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class GameHomeViewModel @Inject constructor(
    private val broadcastPayloadUseCase: BroadcastPayloadUseCase,
    private val getGameStateUseCase: GetGameStateUseCase,
    private val getUserUseCase: GetUserUseCase
) : ViewModel() {
    private val _events = Channel<GameHomeEvent>()
    val events = _events.receiveAsFlow()
    val state = GameHomeState()

    fun handleIntent(intent: GameHomeIntent) {
        when (intent) {
            is GameHomeIntent.Back -> sendEvent(GameHomeEvent.Back)
            is GameHomeIntent.AddDrawing -> viewModelScope.launch {
                val drawing = Annotation.Drawing(
                    path = intent.path,
                    strokeWidth = intent.strokeWidth,
                    color = state.selectedColor.toColor(),
                    createdBy = getUserUseCase().id,
                    id = UUID.randomUUID().toString()
                )
                state.annotations.add(drawing)
                viewModelScope.launch { broadcastPayloadUseCase(drawing) }
            }

            is GameHomeIntent.AddText -> viewModelScope.launch {
                val text = Annotation.Text(
                    text = intent.text,
                    size = intent.size,
                    color = state.selectedColor.toColor(),
                    anchor = intent.anchor,
                    createdBy = getUserUseCase().id,
                    id = UUID.randomUUID().toString()
                )
                state.annotations.add(text)
                broadcastPayloadUseCase(text)
            }

            is GameHomeIntent.SelectColor -> {
                state.selectedColor = intent.color.toComposeColor()
            }

            is GameHomeIntent.SelectOpacity -> {
                state.selectedColor = state.selectedColor.copy(alpha = intent.opacity)
            }

            is GameHomeIntent.SelectSize -> state.selectedSize = intent.size
            is GameHomeIntent.ChangeMode -> state.annotationMode = intent.mode
            is GameHomeIntent.Load -> viewModelScope.launch {
                val gameState = getGameStateUseCase()
                val user = getUserUseCase()
                val isDM = gameState.game.dmId == user.id

                state.isDM = isDM
                state.users = gameState.connectedUsers
                state.annotations.addAll(gameState.gameHomeState.annotations)
                state.allowAnnotations = gameState.gameHomeState.allowEditing || isDM
            }

            is GameHomeIntent.AddImage -> {
                // TODO Add image
            }

            is GameHomeIntent.SelectPlayer -> state.selectedPlayer = intent.player
        }
    }

    private fun sendEvent(event: GameHomeEvent) = viewModelScope.launch { _events.send(event) }
}
