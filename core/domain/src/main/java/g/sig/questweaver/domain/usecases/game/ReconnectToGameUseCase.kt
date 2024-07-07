package g.sig.questweaver.domain.usecases.game

import g.sig.questweaver.domain.repositories.GameRepository
import g.sig.questweaver.domain.usecases.nearby.RequestConnectionUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class ReconnectToGameUseCase(
    private val requestConnectionUseCase: RequestConnectionUseCase,
    private val gameRepository: GameRepository,
    private val defaultDispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(
        gameId: String,
        onReconnectionSuccess: suspend () -> Unit,
        onGameNotFound: suspend () -> Unit,
    ) = withContext(defaultDispatcher) {
        val gameSession = gameRepository.getGame(gameId)

        if (gameSession == null || gameSession.hostDeviceId.isNullOrEmpty()) {
            onGameNotFound()
            return@withContext
        }

        requestConnectionUseCase(
            gameSession.hostDeviceId,
            onConnected = {
                onReconnectionSuccess()
            },
            onError = {
                onGameNotFound()
            },
        )
    }
}
