package g.sig.questweaver.domain.usecases.game

import g.sig.questweaver.domain.repositories.GameRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class GetGameUseCase(
    private val gameRepository: GameRepository,
    private val defaultDispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(gameId: String? = null) =
        withContext(defaultDispatcher) {
            gameRepository.getGame(gameId)
        }
}
