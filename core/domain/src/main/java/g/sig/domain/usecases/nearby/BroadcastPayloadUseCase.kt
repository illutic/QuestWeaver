package g.sig.domain.usecases.nearby

import g.sig.domain.entities.PayloadData
import g.sig.domain.repositories.DeviceRepository
import g.sig.domain.repositories.PayloadRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class BroadcastPayloadUseCase(
    private val payloadRepository: PayloadRepository,
    private val deviceRepository: DeviceRepository,
    private val defaultDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(payload: PayloadData) = withContext(defaultDispatcher) {
        deviceRepository.devices.value.forEach { payloadRepository.send(it.id, payload) }
    }
}