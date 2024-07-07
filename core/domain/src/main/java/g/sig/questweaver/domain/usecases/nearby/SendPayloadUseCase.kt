package g.sig.questweaver.domain.usecases.nearby

import g.sig.questweaver.domain.entities.PayloadData
import g.sig.questweaver.domain.repositories.PayloadRepository

class SendPayloadUseCase(
    private val payloadRepository: PayloadRepository
) {
    operator fun invoke(payload: PayloadData.Unicast) {
        payloadRepository.send(payload.destination, payload)
    }
}
