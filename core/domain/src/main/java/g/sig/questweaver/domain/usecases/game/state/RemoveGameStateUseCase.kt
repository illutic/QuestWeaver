package g.sig.questweaver.domain.usecases.game.state

import g.sig.questweaver.domain.repositories.GameStateRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class RemoveGameStateUseCase(
    private val gameStateRepository: GameStateRepository,
    private val defaultDispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(gameId: String?) =
        withContext(defaultDispatcher) {
            gameStateRepository.removeGameState(gameId)
        }
}
