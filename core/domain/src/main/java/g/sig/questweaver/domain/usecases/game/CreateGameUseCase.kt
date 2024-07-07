package g.sig.questweaver.domain.usecases.game

import g.sig.questweaver.domain.entities.common.Game
import g.sig.questweaver.domain.repositories.GameRepository
import g.sig.questweaver.domain.usecases.game.state.CreateGameStateUseCase
import g.sig.questweaver.domain.usecases.game.state.GetGameStateUseCase
import g.sig.questweaver.domain.usecases.user.GetUserUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class CreateGameUseCase(
    private val getUserUseCase: GetUserUseCase,
    private val getGameStateUseCase: GetGameStateUseCase,
    private val createGameStateUseCase: CreateGameStateUseCase,
    private val gameRepository: GameRepository,
    private val defaultDispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(game: Game) =
        withContext(defaultDispatcher) {
            gameRepository.createGame(game.copy(dmId = game.dmId ?: getUserUseCase().id))
            if (getGameStateUseCase(game.gameId) == null) {
                createGameStateUseCase(game.gameId)
            }
        }
}
