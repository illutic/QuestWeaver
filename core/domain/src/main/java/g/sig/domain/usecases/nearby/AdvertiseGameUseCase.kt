package g.sig.domain.usecases.nearby

import g.sig.domain.repositories.NearbyRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class AdvertiseGameUseCase(
    private val nearbyRepository: NearbyRepository,
    private val mainDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(name: String) = withContext(mainDispatcher) { nearbyRepository.advertiseGame(name) }
}