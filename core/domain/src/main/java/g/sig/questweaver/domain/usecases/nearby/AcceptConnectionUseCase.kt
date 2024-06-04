package g.sig.questweaver.domain.usecases.nearby

import g.sig.questweaver.domain.entities.ConnectionState
import g.sig.questweaver.domain.entities.Device
import g.sig.questweaver.domain.repositories.DeviceRepository
import g.sig.questweaver.domain.repositories.NearbyRepository
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
                        is ConnectionState.Error.DisconnectionError -> deviceRepository.removeDevice(
                            state.endpointId
                        )

                        is ConnectionState.Error.LostError -> deviceRepository.removeDevice(state.endpointId)

                        else -> deviceRepository.updateState(device.id, state)
                    }
                }
        }
    }
}
