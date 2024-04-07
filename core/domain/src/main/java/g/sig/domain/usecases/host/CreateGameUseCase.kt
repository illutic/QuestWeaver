package g.sig.domain.usecases.host

import g.sig.domain.repositories.NearbyRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class CreateGameUseCase(
    private val nearbyRepository: NearbyRepository,
    private val mainDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(name: String, description: String, players: Int) =
        withContext(mainDispatcher) { nearbyRepository.createGame(name, description, players) }
}