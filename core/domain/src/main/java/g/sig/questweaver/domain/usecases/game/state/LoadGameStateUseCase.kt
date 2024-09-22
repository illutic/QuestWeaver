package g.sig.questweaver.domain.usecases.game.state

import g.sig.questweaver.domain.entities.common.Annotation
import g.sig.questweaver.domain.entities.common.RemoveAnnotation
import g.sig.questweaver.domain.entities.states.GameState
import g.sig.questweaver.domain.usecases.nearby.OnPayloadReceivedUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class LoadGameStateUseCase(
    private val getGameStateUseCase: GetGameStateUseCase,
    private val onPayloadReceived: OnPayloadReceivedUseCase,
    private val updateGameStateUseCase: UpdateGameStateUseCase,
    private val defaultDispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(onGameStateLoaded: suspend (GameState) -> Unit) =
        withContext(defaultDispatcher) {
            var state =
                checkNotNull(getGameStateUseCase()) { "The DM should have a game state by now." }

            onGameStateLoaded(state)

            onPayloadReceived { incomingPayload ->
                when (val data = incomingPayload.payloadData.data) {
                    is GameState -> {
                        state = data
                        onGameStateLoaded(state)
                        updateGameStateUseCase(state)
                    }

                    is Annotation -> {
                        state = state.copy(annotations = state.annotations + data)
                        onGameStateLoaded(state)
                        updateGameStateUseCase(state)
                    }

                    is RemoveAnnotation -> {
                        state =
                            state.copy(annotations = state.annotations.filter { it.id != data.id })
                        onGameStateLoaded(state)
                        updateGameStateUseCase(state)
                    }

                    else -> {}
                }
            }
        }
}
