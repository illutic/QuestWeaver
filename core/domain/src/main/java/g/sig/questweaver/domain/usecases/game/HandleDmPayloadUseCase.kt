package g.sig.questweaver.domain.usecases.game

import g.sig.questweaver.domain.entities.DomainEntity
import g.sig.questweaver.domain.entities.IncomingPayload
import g.sig.questweaver.domain.entities.PayloadData
import g.sig.questweaver.domain.entities.common.Annotation
import g.sig.questweaver.domain.entities.common.RemoveAnnotation
import g.sig.questweaver.domain.entities.states.GameState
import g.sig.questweaver.domain.entities.states.RequestGameState
import g.sig.questweaver.domain.usecases.game.state.GetGameStateUseCase
import g.sig.questweaver.domain.usecases.game.state.UpdateGameStateUseCase
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
    private val updateGameStateUseCase: UpdateGameStateUseCase,
    private val defaultDispatcher: CoroutineDispatcher,
) {
    private fun getUpdatedGameState(
        state: GameState,
        domainEntity: DomainEntity,
    ) = when (domainEntity) {
        is Annotation -> state.copy(annotations = state.annotations + domainEntity)
        is RemoveAnnotation -> state.copy(annotations = state.annotations.filter { it.id != domainEntity.id })
        else -> state
    }

    suspend operator fun invoke(incomingPayload: IncomingPayload) =
        withContext(defaultDispatcher) {
            val user = getUserUseCase()
            val dmId = getDmIdUseCase()
            var gameState = getGameStateUseCase() ?: return@withContext
            if (dmId != user.id) return@withContext

            when (val payloadData = incomingPayload.payloadData) {
                is PayloadData.Broadcast -> {
                    broadcastPayloadUseCase(payloadData.data)
                    gameState = getUpdatedGameState(gameState, payloadData.data)
                    updateGameStateUseCase(gameState)
                }

                is PayloadData.Unicast -> {
                    when (payloadData.data) {
                        is RequestGameState -> sendPayloadUseCase(incomingPayload.origin, gameState)
                        else -> sendPayloadUseCase(payloadData)
                    }
                }
            }
        }
}
