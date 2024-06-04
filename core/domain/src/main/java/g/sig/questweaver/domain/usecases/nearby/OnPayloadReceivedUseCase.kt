package g.sig.questweaver.domain.usecases.nearby

import g.sig.questweaver.domain.entities.PayloadData
import g.sig.questweaver.domain.repositories.PayloadRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class OnPayloadReceivedUseCase(
    val payloadRepository: PayloadRepository,
    val defaultDispatcher: CoroutineDispatcher
) {

    suspend inline operator fun invoke(crossinline onPayload: (PayloadData) -> Unit) =
        withContext(defaultDispatcher) {
            payloadRepository.data.collect { onPayload(it) }
        }
}
