package g.sig.questweaver.domain.usecases.game

import g.sig.questweaver.domain.entities.common.Game
import g.sig.questweaver.domain.repositories.GameSessionRepository
import g.sig.questweaver.domain.usecases.user.GetUserUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class CreateGameSessionUseCase(
    private val getUserUseCase: GetUserUseCase,
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
                    maxPlayers = numberOfPlayers,
                    dmId = getUserUseCase().id
                )
            )
        }
}
