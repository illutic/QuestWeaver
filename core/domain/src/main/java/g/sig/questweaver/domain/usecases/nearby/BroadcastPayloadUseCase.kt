package g.sig.questweaver.domain.usecases.nearby

import g.sig.questweaver.domain.entities.io.PayloadData
import g.sig.questweaver.domain.repositories.DeviceRepository
import g.sig.questweaver.domain.repositories.PayloadRepository
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
