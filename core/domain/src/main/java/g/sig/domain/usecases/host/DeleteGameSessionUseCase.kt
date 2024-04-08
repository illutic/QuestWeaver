package g.sig.domain.usecases.host

import g.sig.domain.repositories.GameSessionRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DeleteGameSessionUseCase(
    private val gameSessionRepository: GameSessionRepository,
    private val defaultDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke() = withContext(defaultDispatcher) {
        gameSessionRepository.endGameSession()
    }
}