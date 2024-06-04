package g.sig.questweaver.data.repositories

import g.sig.questweaver.data.datasources.game.GameSessionDataSource
import g.sig.questweaver.data.datasources.recentgames.RecentGamesDataSource
import g.sig.questweaver.data.entities.Game.Companion.fromDomain
import g.sig.questweaver.domain.entities.Game
import g.sig.questweaver.domain.repositories.GameSessionRepository

class GameSessionRepositoryImpl(
    private val dataSource: GameSessionDataSource,
    private val recentGamesDataSource: RecentGamesDataSource
) : GameSessionRepository {
    override suspend fun getGameSession(): Game = dataSource.currentGameSession.value.toDomain()

    override suspend fun startGameSession(game: Game) =
        dataSource.startGameSession(game.fromDomain())

    override suspend fun updateGameSession(game: Game) {
        dataSource.updateGameSession(game.fromDomain())
        recentGamesDataSource.saveGame(game.fromDomain())
    }

    override suspend fun endGameSession() = dataSource.endGameSession()
}
