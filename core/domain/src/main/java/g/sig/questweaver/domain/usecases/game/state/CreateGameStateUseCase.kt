package g.sig.questweaver.domain.usecases.game.state

import g.sig.questweaver.domain.entities.common.Annotation
import g.sig.questweaver.domain.entities.common.User
import g.sig.questweaver.domain.entities.states.GameState
import g.sig.questweaver.domain.repositories.GameStateRepository
import g.sig.questweaver.domain.usecases.game.GetGameUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class CreateGameStateUseCase(
    private val getGameUseCase: GetGameUseCase,
    private val gameStateRepository: GameStateRepository,
    private val defaultDispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(gameState: GameState) =
        withContext(defaultDispatcher) {
            gameStateRepository.createGameState(gameState)
        }

    suspend operator fun invoke(
        gameId: String? = null,
        annotations: List<Annotation> = emptyList(),
        allowEditing: Boolean = true,
        connectedUsers: List<User> = emptyList(),
    ) = withContext(defaultDispatcher) {
        val safeGameId = gameId ?: getGameUseCase()?.gameId ?: return@withContext
        gameStateRepository.createGameState(
            GameState(
                safeGameId,
                connectedUsers,
                annotations,
                allowEditing,
            ),
        )
    }
}
