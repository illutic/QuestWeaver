package g.sig.domain.usecases.nearby

import g.sig.domain.entities.PayloadData
import g.sig.domain.repositories.PayloadRepository

class SendPayloadUseCase(
    private val payloadRepository: PayloadRepository
) {
    operator fun invoke(endpointId: String, payload: PayloadData) {
        payloadRepository.send(endpointId, payload)
    }
}