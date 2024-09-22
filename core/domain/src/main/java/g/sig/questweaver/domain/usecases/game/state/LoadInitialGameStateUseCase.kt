package g.sig.questweaver.domain.usecases.game.state

import g.sig.questweaver.domain.entities.states.RequestGameState
import g.sig.questweaver.domain.usecases.game.GetGameUseCase
import g.sig.questweaver.domain.usecases.nearby.BroadcastPayloadUseCase
import g.sig.questweaver.domain.usecases.nearby.SendPayloadUseCase
import g.sig.questweaver.domain.usecases.user.GetUserUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class LoadInitialGameStateUseCase(
    private val getGameStateUseCase: GetGameStateUseCase,
    private val getGameUseCase: GetGameUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val broadcastPayloadUseCase: BroadcastPayloadUseCase,
    private val sendPayloadUseCase: SendPayloadUseCase,
    private val defaultDispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke() =
        withContext(defaultDispatcher) {
            val game = getGameUseCase()
            val isDm = getUserUseCase().id == game.dmId
            val state =
                checkNotNull(getGameStateUseCase()) {
                    "The DM should have a game state by now."
                }

            if (isDm) {
                broadcastPayloadUseCase(state)
            } else {
                sendPayloadUseCase(game.hostDeviceId!!, RequestGameState)
            }
        }
}
