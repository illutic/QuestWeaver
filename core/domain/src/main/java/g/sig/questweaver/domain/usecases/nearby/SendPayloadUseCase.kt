package g.sig.questweaver.domain.usecases.nearby

import g.sig.questweaver.domain.entities.io.PayloadData
import g.sig.questweaver.domain.repositories.PayloadRepository

class SendPayloadUseCase(
    private val payloadRepository: PayloadRepository
) {
    operator fun invoke(endpointId: String, payload: PayloadData) {
        payloadRepository.send(endpointId, payload)
    }
}
