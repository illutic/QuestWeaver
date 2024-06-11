package g.sig.questweaver.domain.usecases.game

import g.sig.questweaver.domain.entities.states.GameHomeState
import g.sig.questweaver.domain.entities.states.GameState
import g.sig.questweaver.domain.repositories.GameSessionRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class GetGameStateUseCase(
    private val gameSessionRepository: GameSessionRepository,
    private val defaultDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke() = withContext(defaultDispatcher) {
        val gameSession = gameSessionRepository.getGameSession()

        GameState(
            game = gameSession,
            connectedUsers = emptyList(), // TODO add use case
            gameHomeState = GameHomeState.Empty, // TODO add use case
        )
    }
}
