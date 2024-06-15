package g.sig.questweaver.domain.usecases.nearby

import g.sig.questweaver.domain.entities.DomainEntity
import g.sig.questweaver.domain.repositories.PayloadRepository

class SendPayloadUseCase(
    private val payloadRepository: PayloadRepository
) {
    operator fun invoke(endpointId: String, payload: DomainEntity) {
        payloadRepository.send(endpointId, payload)
    }
}
