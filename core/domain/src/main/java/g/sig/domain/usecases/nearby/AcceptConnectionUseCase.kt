package g.sig.domain.usecases.nearby

import g.sig.domain.entities.ConnectionState
import g.sig.domain.entities.Device
import g.sig.domain.repositories.DeviceRepository
import g.sig.domain.repositories.NearbyRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class AcceptConnectionUseCase(
    private val nearbyRepository: NearbyRepository,
    private val deviceRepository: DeviceRepository,
    private val coroutineScope: CoroutineScope
) {
    operator fun invoke(device: Device) {
        coroutineScope.launch {
            nearbyRepository
                .acceptConnection(device)
                .collect { state ->
                    when (state) {
                        is ConnectionState.Error.DisconnectionError -> deviceRepository.removeDevice(state.endpointId)

                        is ConnectionState.Error.LostError -> deviceRepository.removeDevice(state.endpointId)

                        else -> deviceRepository.updateState(device.id, state)
                    }
                }
        }
    }
}