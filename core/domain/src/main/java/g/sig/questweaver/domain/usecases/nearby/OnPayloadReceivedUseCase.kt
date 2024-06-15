package g.sig.questweaver.domain.usecases.nearby

import g.sig.questweaver.domain.entities.DomainEntity
import g.sig.questweaver.domain.repositories.PayloadRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class OnPayloadReceivedUseCase(
    val payloadRepository: PayloadRepository,
    val defaultDispatcher: CoroutineDispatcher
) {

    suspend inline operator fun invoke(crossinline onPayload: (DomainEntity) -> Unit) =
        withContext(defaultDispatcher) {
            payloadRepository.data.collect { onPayload(it) }
        }
}
