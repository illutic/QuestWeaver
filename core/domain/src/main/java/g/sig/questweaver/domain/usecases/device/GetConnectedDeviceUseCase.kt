package g.sig.questweaver.domain.usecases.device

import g.sig.questweaver.domain.entities.states.ConnectionState
import g.sig.questweaver.domain.repositories.DeviceRepository
import kotlinx.coroutines.flow.map

class GetConnectedDeviceUseCase(
    private val repository: DeviceRepository,
) {
    operator fun invoke() =
        repository.devices.map {
            it.find { device ->
                device.connectionState is ConnectionState.Connected
            }
        }
}
