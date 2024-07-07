package g.sig.questweaver.domain.usecases.game.state

import g.sig.questweaver.domain.entities.common.Annotation
import g.sig.questweaver.domain.entities.common.User
import g.sig.questweaver.domain.entities.states.GameState
import g.sig.questweaver.domain.repositories.GameStateRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class UpdateGameStateUseCase(
    private val getGameStateUseCase: GetGameStateUseCase,
    private val gameStateRepository: GameStateRepository,
    private val defaultDispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(gameState: GameState) =
        withContext(defaultDispatcher) {
            gameStateRepository.updateGameState(gameState)
        }

    suspend operator fun invoke(
        annotations: List<Annotation> = emptyList(),
        allowEditing: Boolean = true,
        connectedUsers: List<User> = emptyList(),
    ) = withContext(defaultDispatcher) {
        val gameState = getGameStateUseCase() ?: return@withContext
        gameStateRepository.updateGameState(
            gameState.copy(
                annotations = annotations,
                allowEditing = allowEditing,
                connectedUsers = connectedUsers,
            ),
        )
    }
}
