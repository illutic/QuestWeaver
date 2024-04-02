package g.sig.domain.usecases.nearby

import g.sig.domain.repositories.NearbyGamesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn

class GetNearbyGamesUseCase(
    private val nearbyRepository: NearbyGamesRepository,
    private val mainDispatcher: CoroutineDispatcher
) {
    operator fun invoke() = nearbyRepository.findNearbyGames().flowOn(mainDispatcher)
}