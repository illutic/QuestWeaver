package g.sig.questweaver.data.repositories

import g.sig.questweaver.data.datasources.game.GameSessionDataSource
import g.sig.questweaver.data.datasources.recentgames.RecentGamesDataSource
import g.sig.questweaver.data.mapper.toDomain
import g.sig.questweaver.data.mapper.toDto
import g.sig.questweaver.domain.entities.states.GameState
import g.sig.questweaver.domain.repositories.GameSessionRepository
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class GameSessionRepositoryImpl(
    private val dataSource: GameSessionDataSource,
    private val recentGamesDataSource: RecentGamesDataSource
) : GameSessionRepository {
    override suspend fun getGameSession() = dataSource.currentGameSessionDto.value.toDomain()

    override suspend fun startGameSession(game: GameState) =
        dataSource.startGameSession(game.toDto())

    override suspend fun updateGameSession(game: GameState): Unit = coroutineScope {
        dataSource.updateGameSession(game.toDto())
        launch {
            recentGamesDataSource.saveGame(game.toDto().game)
        }
    }

    override suspend fun endGameSession() = dataSource.endGameSession()
}
