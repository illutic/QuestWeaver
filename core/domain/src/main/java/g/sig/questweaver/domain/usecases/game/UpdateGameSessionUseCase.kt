package g.sig.questweaver.domain.usecases.game

import g.sig.questweaver.domain.entities.common.Annotation
import g.sig.questweaver.domain.repositories.GameSessionRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class UpdateGameSessionUseCase(
    private val getGameStateUseCase: GetGameStateUseCase,
    private val gameSessionRepository: GameSessionRepository,
    private val defaultDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(
        annotations: List<Annotation>,
    ) = withContext(defaultDispatcher) {
        val gameState = getGameStateUseCase()
        val updatedGameState =
            gameState.copy(gameHomeState = gameState.gameHomeState.copy(annotations = annotations))
        gameSessionRepository.updateGameSession(updatedGameState)
    }
}
