package g.sig.data.repositories

import g.sig.data.datasources.game.GameSessionDataSource
import g.sig.data.entities.Game.Companion.fromDomain
import g.sig.domain.entities.Game
import g.sig.domain.repositories.GameSessionRepository

class GameSessionRepositoryImpl(
    private val dataSource: GameSessionDataSource
) : GameSessionRepository {
    override suspend fun getGameSession(): Game = dataSource.currentGameSession.value.toDomain()

    override suspend fun startGameSession(game: Game) = dataSource.startGameSession(game.fromDomain())

    override suspend fun updateGameSession(game: Game) = dataSource.updateGameSession(game.fromDomain())

    override suspend fun endGameSession() = dataSource.endGameSession()
}