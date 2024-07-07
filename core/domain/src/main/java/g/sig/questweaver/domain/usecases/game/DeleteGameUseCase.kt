package g.sig.questweaver.domain.usecases.game

import g.sig.questweaver.domain.repositories.GameRepository
import g.sig.questweaver.domain.usecases.game.state.RemoveGameStateUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DeleteGameUseCase(
    private val gameRepository: GameRepository,
    private val removeGameStateUseCase: RemoveGameStateUseCase,
    private val defaultDispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(gameId: String? = null) =
        withContext(defaultDispatcher) {
            gameRepository.removeGame(gameId)
            removeGameStateUseCase(gameId)
        }
}
