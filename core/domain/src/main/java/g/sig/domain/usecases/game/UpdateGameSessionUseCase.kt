package g.sig.domain.usecases.game

import g.sig.domain.entities.Game
import g.sig.domain.repositories.GameSessionRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class UpdateGameSessionUseCase(
    private val gameSessionRepository: GameSessionRepository,
    private val defaultDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(game: Game) = withContext(defaultDispatcher) { gameSessionRepository.updateGameSession(game) }
}