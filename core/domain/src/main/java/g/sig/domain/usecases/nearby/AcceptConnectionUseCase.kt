package g.sig.domain.usecases.nearby

import g.sig.domain.repositories.NearbyRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class AcceptConnectionUseCase(
    private val nearbyRepository: NearbyRepository,
    private val defaultDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(endpointId: String) = withContext(defaultDispatcher) { nearbyRepository.acceptConnection(endpointId) }
}