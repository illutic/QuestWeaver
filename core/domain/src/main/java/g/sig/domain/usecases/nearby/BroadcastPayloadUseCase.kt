package g.sig.domain.usecases.nearby

import g.sig.domain.entities.PayloadData
import g.sig.domain.repositories.DeviceRepository
import g.sig.domain.repositories.PayloadRepository

class BroadcastPayloadUseCase(
    private val payloadRepository: PayloadRepository,
    private val deviceRepository: DeviceRepository
) {
    operator fun invoke(payload: PayloadData) {
        deviceRepository.getDevices().forEach { payloadRepository.send(it.id, payload) }
    }
}