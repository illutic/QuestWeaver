package g.sig.questweaver.domain.usecases.nearby

import g.sig.questweaver.domain.entities.IncomingPayload
import g.sig.questweaver.domain.repositories.PayloadRepository
import g.sig.questweaver.domain.usecases.game.HandleDmPayloadUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class OnPayloadReceivedUseCase(
    val handleDmPayloadUseCase: HandleDmPayloadUseCase,
    val payloadRepository: PayloadRepository,
    val defaultDispatcher: CoroutineDispatcher,
) {
    suspend inline operator fun invoke(crossinline onPayload: suspend (IncomingPayload) -> Unit) =
        withContext(defaultDispatcher) {
            payloadRepository.data.collect {
                handleDmPayloadUseCase(it)
                onPayload(it)
            }
        }
}
