package g.sig.domain.usecases.nearby

import g.sig.domain.entities.PayloadData
import g.sig.domain.repositories.PayloadRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

//TODO make payload listening a service
class OnPayloadReceivedUseCase(
    val payloadRepository: PayloadRepository,
    val defaultDispatcher: CoroutineDispatcher
) {

    suspend inline operator fun invoke(crossinline onPayload: (PayloadData) -> Unit) = withContext(defaultDispatcher) {
        payloadRepository.data.collect { onPayload(it) }
    }
}