package g.sig.questweaver.domain.usecases.game.state

import g.sig.questweaver.domain.entities.states.GameState
import g.sig.questweaver.domain.repositories.GameStateRepository
import g.sig.questweaver.domain.usecases.game.GetGameUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class GetGameStateUseCase(
    private val getGameUseCase: GetGameUseCase,
    private val gameStateRepository: GameStateRepository,
    private val defaultDispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(gameId: String? = null): GameState? =
        withContext(defaultDispatcher) {
            gameId?.let { gameStateRepository.getGameState(it) }
                ?: getGameUseCase()?.let { gameStateRepository.getGameState(it.gameId) }
        }
}
