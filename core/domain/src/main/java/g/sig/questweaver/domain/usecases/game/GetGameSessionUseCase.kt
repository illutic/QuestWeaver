package g.sig.questweaver.domain.usecases.game

import g.sig.questweaver.domain.repositories.GameSessionRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class GetGameSessionUseCase(
    private val gameSessionRepository: GameSessionRepository,
    private val defaultDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke() = withContext(defaultDispatcher) {
        gameSessionRepository.getGameSession()
    }
}
