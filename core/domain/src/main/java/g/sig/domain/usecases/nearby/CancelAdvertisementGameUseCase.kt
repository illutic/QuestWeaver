package g.sig.domain.usecases.nearby

import g.sig.domain.repositories.NearbyRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class CancelAdvertisementGameUseCase(
    private val nearbyRepository: NearbyRepository,
    private val mainDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke() =
        withContext(mainDispatcher) { nearbyRepository.cancelAdvertisement() }
}