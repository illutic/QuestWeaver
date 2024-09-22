package g.sig.questweaver.domain.usecases.game

import g.sig.questweaver.domain.entities.common.Game
import g.sig.questweaver.domain.repositories.GameRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class GetGameUseCase(
    private val gameRepository: GameRepository,
    private val defaultDispatcher: CoroutineDispatcher,
) {
    private var game: Game = Game.Empty

    suspend operator fun invoke(gameId: String? = null) =
        withContext(defaultDispatcher) {
            if (game == Game.Empty) {
                game = gameRepository.getGame(gameId) ?: Game.Empty
            }
            game
        }
}
