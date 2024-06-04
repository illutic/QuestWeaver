package g.sig.questweaver.domain.usecases.game

import g.sig.questweaver.domain.entities.Game
import g.sig.questweaver.domain.repositories.GameSessionRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class CreateGameSessionUseCase(
    private val gameSessionRepository: GameSessionRepository,
    private val defaultDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(
        id: String,
        name: String,
        description: String,
        numberOfPlayers: Int
    ) =
        withContext(defaultDispatcher) {
            gameSessionRepository.startGameSession(
                Game(
                    gameId = id,
                    title = name,
                    description = description,
                    maxPlayers = numberOfPlayers
                )
            )
        }
}
