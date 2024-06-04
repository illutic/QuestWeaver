package g.sig.questweaver.domain.usecases.nearby

import g.sig.questweaver.domain.entities.ConnectionState
import g.sig.questweaver.domain.repositories.DeviceRepository
import g.sig.questweaver.domain.repositories.NearbyRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class AdvertiseGameUseCase(
    private val nearbyRepository: NearbyRepository,
    private val deviceRepository: DeviceRepository,
    private val mainDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(name: String) =
        withContext(mainDispatcher) {
            nearbyRepository
                .advertiseGame(name)
                .collect { state ->
                    when (state) {
                        is ConnectionState.Connecting -> deviceRepository.addDevice(
                            state.endpointId,
                            state.name
                        )

                        is ConnectionState.Error.DisconnectionError -> deviceRepository.removeDevice(
                            state.endpointId
                        )

                        else -> Unit
                    }
                }
        }
}
