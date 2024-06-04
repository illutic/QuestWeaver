package g.sig.questweaver.domain.usecases.nearby

import g.sig.questweaver.domain.entities.ConnectionState
import g.sig.questweaver.domain.entities.Device
import g.sig.questweaver.domain.repositories.DeviceRepository
import g.sig.questweaver.domain.repositories.NearbyRepository
import g.sig.questweaver.domain.usecases.user.GetUserUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class RequestConnectionUseCase(
    private val stopDiscovery: CancelDiscoveryUseCase,
    private val nearbyRepository: NearbyRepository,
    private val deviceRepository: DeviceRepository,
    private val getUser: GetUserUseCase,
    private val defaultDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(device: Device) {
        withContext(defaultDispatcher) {
            nearbyRepository
                .requestConnection(getUser(), device)
                .collect { state ->
                    when (state) {
                        is ConnectionState.Connected -> {
                            stopDiscovery()
                            deviceRepository.updateState(device.id, state)
                            deviceRepository.devices.value
                                .forEach {
                                    if (it.connectionState !is ConnectionState.Connected) {
                                        deviceRepository.removeDevice(it.id)
                                    }
                                }
                        }

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
