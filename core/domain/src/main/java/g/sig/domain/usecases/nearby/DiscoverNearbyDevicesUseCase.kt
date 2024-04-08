package g.sig.domain.usecases.nearby

import g.sig.domain.repositories.NearbyRepository
import g.sig.domain.usecases.user.GetUserUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DiscoverNearbyDevicesUseCase(
    private val nearbyRepository: NearbyRepository,
    private val getUser: GetUserUseCase,
    private val mainDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke() = withContext(mainDispatcher) { nearbyRepository.discoverNearbyDevices(getUser()) }
}