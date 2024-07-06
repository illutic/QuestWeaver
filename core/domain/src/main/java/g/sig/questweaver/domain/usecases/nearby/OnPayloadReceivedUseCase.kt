package g.sig.questweaver.domain.usecases.nearby

import g.sig.questweaver.domain.entities.DomainEntity
import g.sig.questweaver.domain.repositories.PayloadRepository
import g.sig.questweaver.domain.usecases.game.GetGameStateUseCase
import g.sig.questweaver.domain.usecases.user.GetUserUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class OnPayloadReceivedUseCase(
    val getUserUseCase: GetUserUseCase,
    val getGameStateUseCase: GetGameStateUseCase,
    val broadcastPayloadUseCase: BroadcastPayloadUseCase,
    val payloadRepository: PayloadRepository,
    val defaultDispatcher: CoroutineDispatcher
) {

    suspend inline operator fun invoke(crossinline onPayload: (DomainEntity) -> Unit) =
        withContext(defaultDispatcher) {
            val user = getUserUseCase()
            val isDm = getGameStateUseCase().game.dmId == user.id
            payloadRepository.data.collect {
                onPayload(it)
                if (isDm) broadcastPayloadUseCase(it)
            }
        }
}
