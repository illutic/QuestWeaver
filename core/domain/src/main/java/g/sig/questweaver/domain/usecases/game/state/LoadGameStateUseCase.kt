package g.sig.questweaver.domain.usecases.game.state

import g.sig.questweaver.domain.entities.common.Game
import g.sig.questweaver.domain.entities.states.GameState
import g.sig.questweaver.domain.entities.states.RequestGameState
import g.sig.questweaver.domain.usecases.game.GetGameUseCase
import g.sig.questweaver.domain.usecases.game.UpdateGameUseCase
import g.sig.questweaver.domain.usecases.nearby.OnPayloadReceivedUseCase
import g.sig.questweaver.domain.usecases.nearby.SendPayloadUseCase
import g.sig.questweaver.domain.usecases.user.GetUserUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class LoadGameStateUseCase(
    val getGameUseCase: GetGameUseCase,
    val getUserUseCase: GetUserUseCase,
    val getGameStateUseCase: GetGameStateUseCase,
    val sendPayloadUseCase: SendPayloadUseCase,
    val onPayloadReceived: OnPayloadReceivedUseCase,
    val createOrUpdateGameStateUseCase: CreateOrUpdateGameStateUseCase,
    val updateGameUseCase: UpdateGameUseCase,
    val defaultDispatcher: CoroutineDispatcher,
) {
    suspend inline operator fun invoke(crossinline onGameStateLoaded: suspend (GameState) -> Unit) =
        withContext(defaultDispatcher) {
            val game = getGameUseCase() ?: return@withContext
            val user = getUserUseCase()
            val isDm = game.dmId == user.id

            if (isDm) {
                val state =
                    checkNotNull(getGameStateUseCase(game.gameId)) {
                        "The DM should have a game state by now."
                    }

                onGameStateLoaded(state)

                onPayloadReceived {
                    val data = it.payloadData.data
                    if (data is GameState) onGameStateLoaded(data)
                }
            } else {
                val hostDeviceId =
                    checkNotNull(game.hostDeviceId) {
                        "The non-DM should have a host device ID by now."
                    }

                sendPayloadUseCase(hostDeviceId, RequestGameState)

                onPayloadReceived {
                    when (val data = it.payloadData.data) {
                        is GameState -> {
                            createOrUpdateGameStateUseCase(data)
                            onGameStateLoaded(data)
                        }

                        is Game -> updateGameUseCase(data)
                        else -> Unit
                    }
                }
            }
        }
}
