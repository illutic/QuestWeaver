package g.sig.questweaver.domain.usecases.game

import g.sig.questweaver.domain.entities.states.GameState
import g.sig.questweaver.domain.repositories.GameSessionRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class UpdateGameSessionUseCase(
    private val gameSessionRepository: GameSessionRepository,
    private val defaultDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(gameState: GameState) = withContext(defaultDispatcher) {
        gameSessionRepository.updateGameSession(gameState)
    }
}
