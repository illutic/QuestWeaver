package g.sig.questweaver.domain.usecases.game.state

import g.sig.questweaver.domain.entities.common.Annotation
import g.sig.questweaver.domain.entities.common.User
import g.sig.questweaver.domain.entities.states.GameState
import g.sig.questweaver.domain.usecases.game.GetGameUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class CreateOrUpdateGameStateUseCase(
    private val getGameUseCase: GetGameUseCase,
    private val createGameStateUseCase: CreateGameStateUseCase,
    private val updateGameStateUseCase: UpdateGameStateUseCase,
    private val getGameStateUseCase: GetGameStateUseCase,
    private val defaultDispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(
        annotations: List<Annotation> = emptyList(),
        allowEditing: Boolean = true,
        connectedUsers: List<User> = emptyList(),
    ) = withContext(defaultDispatcher) {
        val game = getGameUseCase() ?: return@withContext
        val gameState =
            GameState(
                game.gameId,
                connectedUsers,
                annotations,
                allowEditing,
            )
        val gameStateExists = getGameStateUseCase(game.gameId) != null
        if (gameStateExists) {
            updateGameStateUseCase(gameState)
        } else {
            createGameStateUseCase(gameState)
        }
    }

    suspend operator fun invoke(gameState: GameState) =
        withContext(defaultDispatcher) {
            val game = getGameUseCase()
            val gameStateExists = getGameStateUseCase(game?.gameId) != null
            if (gameStateExists) {
                updateGameStateUseCase(gameState)
            } else {
                createGameStateUseCase(gameState)
            }
        }
}
