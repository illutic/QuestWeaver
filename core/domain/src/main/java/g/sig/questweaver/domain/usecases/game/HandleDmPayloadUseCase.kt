package g.sig.questweaver.domain.usecases.game

import g.sig.questweaver.domain.entities.IncomingPayload
import g.sig.questweaver.domain.entities.PayloadData
import g.sig.questweaver.domain.entities.states.RequestGameState
import g.sig.questweaver.domain.usecases.game.state.GetGameStateUseCase
import g.sig.questweaver.domain.usecases.nearby.BroadcastPayloadUseCase
import g.sig.questweaver.domain.usecases.nearby.SendPayloadUseCase
import g.sig.questweaver.domain.usecases.user.GetUserUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class HandleDmPayloadUseCase(
    private val getUserUseCase: GetUserUseCase,
    private val getDmIdUseCase: GetDmIdUseCase,
    private val broadcastPayloadUseCase: BroadcastPayloadUseCase,
    private val sendPayloadUseCase: SendPayloadUseCase,
    private val getGameStateUseCase: GetGameStateUseCase,
    private val defaultDispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(incomingPayload: IncomingPayload) =
        withContext(defaultDispatcher) {
            val user = getUserUseCase()
            val dmId = getDmIdUseCase()
            if (dmId != user.id) return@withContext

            handlePayload(incomingPayload)
        }

    private suspend fun handlePayload(incomingPayload: IncomingPayload) =
        withContext(defaultDispatcher) {
            val gameState = getGameStateUseCase() ?: return@withContext
            when (val payloadData = incomingPayload.payloadData) {
                is PayloadData.Broadcast ->
                    when (val payload = payloadData.data) {
                        is RequestGameState -> broadcastPayloadUseCase(gameState)
                        else -> broadcastPayloadUseCase(payload)
                    }

                is PayloadData.Unicast ->
                    when (payloadData.data) {
                        is RequestGameState -> sendPayloadUseCase(incomingPayload.origin, gameState)
                        else -> sendPayloadUseCase(payloadData)
                    }
            }
        }
}
