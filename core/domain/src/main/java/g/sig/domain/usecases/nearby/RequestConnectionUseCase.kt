package g.sig.domain.usecases.nearby

import g.sig.domain.entities.Device
import g.sig.domain.repositories.NearbyRepository
import g.sig.domain.usecases.user.GetUserUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class RequestConnectionUseCase(
    private val nearbyRepository: NearbyRepository,
    private val getUser: GetUserUseCase,
    private val defaultDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(device: Device) = withContext(defaultDispatcher) {
        nearbyRepository.requestConnection(getUser(), device)
    }
}