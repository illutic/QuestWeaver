package g.sig.questweaver.domain.usecases.nearby

import g.sig.questweaver.domain.entities.Device
import g.sig.questweaver.domain.repositories.DeviceRepository
import g.sig.questweaver.domain.repositories.NearbyRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class RejectConnectionUseCase(
    private val deviceRepository: DeviceRepository,
    private val nearbyRepository: NearbyRepository,
    private val defaultDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(device: Device) =
        withContext(defaultDispatcher) {
            nearbyRepository.rejectConnection(device).collect {
                deviceRepository.updateState(it)
            }
        }
}
