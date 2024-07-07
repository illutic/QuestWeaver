package g.sig.questweaver.domain.usecases.nearby

import g.sig.questweaver.domain.entities.DomainEntity
import g.sig.questweaver.domain.entities.PayloadData
import g.sig.questweaver.domain.repositories.DeviceRepository
import g.sig.questweaver.domain.repositories.PayloadRepository
import g.sig.questweaver.domain.usecases.game.GetDmIdUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class SendPayloadUseCase(
    private val getDmIdUseCase: GetDmIdUseCase,
    private val deviceRepository: DeviceRepository,
    private val payloadRepository: PayloadRepository,
    private val defaultDispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(
        deviceId: String,
        data: DomainEntity,
    ) = withContext(defaultDispatcher) {
        payloadRepository.send(deviceId, PayloadData.Unicast(data, deviceId))
    }

    suspend operator fun invoke(payload: PayloadData.Unicast) =
        withContext(defaultDispatcher) {
            val dmId = getDmIdUseCase()
            val device = deviceRepository.getDevice(payload.destination)
            if (device == null && dmId != null) {
                // We can't find the device, so let's send it to the DM so they can forward it
                payloadRepository.send(dmId, payload)
            } else {
                payloadRepository.send(payload.destination, payload)
            }
        }
}
