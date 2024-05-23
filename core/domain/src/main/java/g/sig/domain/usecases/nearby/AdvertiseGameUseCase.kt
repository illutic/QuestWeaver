package g.sig.domain.usecases.nearby

import g.sig.domain.entities.ConnectionState
import g.sig.domain.repositories.DeviceRepository
import g.sig.domain.repositories.NearbyRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext

// TODO make this a service?
class AdvertiseGameUseCase(
    private val nearbyRepository: NearbyRepository,
    private val deviceRepository: DeviceRepository,
    private val mainDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(name: String) =
        withContext(mainDispatcher) {
            nearbyRepository
                .advertiseGame(name)
                .onEach { state ->
                    when (state) {
                        is ConnectionState.Connecting -> deviceRepository.addDevice(state.endpointId, state.name)

                        is ConnectionState.Error.DisconnectionError -> deviceRepository.removeDevice(state.endpointId)

                        else -> Unit
                    }
                }
                .map { deviceRepository.getDevices() }
        }
}