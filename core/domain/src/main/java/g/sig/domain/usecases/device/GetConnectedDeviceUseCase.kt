package g.sig.domain.usecases.device

import g.sig.domain.entities.ConnectionState
import g.sig.domain.repositories.DeviceRepository
import kotlinx.coroutines.flow.map

class GetConnectedDeviceUseCase(private val repository: DeviceRepository) {

    operator fun invoke() = repository.devices.map {
        it.find { device ->
            device.connectionState is ConnectionState.Connected
        }
    }
}