package g.sig.questweaver.domain.usecases.nearby

import g.sig.questweaver.domain.entities.states.ConnectionState
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
    private val defaultDispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(
        deviceId: String,
        onConnected: suspend () -> Unit = {},
        onError: suspend () -> Unit = {},
    ) {
        withContext(defaultDispatcher) {
            nearbyRepository
                .requestConnection(getUser(), deviceId)
                .collect { state ->
                    when (state) {
                        is ConnectionState.Connected -> {
                            stopDiscovery()
                            deviceRepository.devices.value
                                .filter { it.id != deviceId }
                                .forEach {
                                    deviceRepository.removeDevice(it.id)
                                }
                            deviceRepository.updateState(deviceId, state)

                            onConnected()
                        }

                        is ConnectionState.Error -> {
                            deviceRepository.removeDevice(deviceId)
                            onError()
                        }

                        else -> deviceRepository.updateState(deviceId, state)
                    }
                }
        }
    }
}
