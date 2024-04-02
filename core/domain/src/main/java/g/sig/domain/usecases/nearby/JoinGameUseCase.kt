package g.sig.domain.usecases.nearby

import g.sig.domain.entities.Game
import g.sig.domain.repositories.NearbyGamesRepository
import g.sig.domain.usecases.user.GetUserUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class JoinGameUseCase(
    private val getUserUseCase: GetUserUseCase,
    private val nearbyRepository: NearbyGamesRepository,
    private val mainDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(game: Game) = withContext(mainDispatcher) {
        nearbyRepository.joinGame(getUserUseCase(), game).flowOn(mainDispatcher)
    }
}