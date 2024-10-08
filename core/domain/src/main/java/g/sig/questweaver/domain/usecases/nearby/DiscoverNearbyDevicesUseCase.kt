package g.sig.questweaver.domain.usecases.nearby

import g.sig.questweaver.domain.entities.states.ConnectionState
import g.sig.questweaver.domain.repositories.DeviceRepository
import g.sig.questweaver.domain.repositories.NearbyRepository
import g.sig.questweaver.domain.usecases.user.GetUserUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DiscoverNearbyDevicesUseCase(
    private val nearbyRepository: NearbyRepository,
    private val deviceRepository: DeviceRepository,
    private val getUser: GetUserUseCase,
    private val mainDispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke() =
        withContext(mainDispatcher) {
            nearbyRepository
                .discoverNearbyDevices(getUser())
                .collect { state ->
                    when (state) {
                        is ConnectionState.Found ->
                            deviceRepository.addDevice(
                                state.endpointId,
                                state.name,
                            )

                        is ConnectionState.Error.DisconnectionError ->
                            deviceRepository.removeDevice(
                                state.endpointId,
                            )

                        is ConnectionState.Error.LostError -> deviceRepository.removeDevice(state.endpointId)

                        else -> Unit
                    }
                }
        }
}
