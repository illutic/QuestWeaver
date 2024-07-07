package g.sig.questweaver.domain.usecases.game

import g.sig.questweaver.domain.entities.common.Game
import g.sig.questweaver.domain.repositories.GameRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class UpdateGameUseCase(
    private val gameRepository: GameRepository,
    private val defaultDispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(game: Game) =
        withContext(defaultDispatcher) {
            gameRepository.updateGame(game)
        }
}
