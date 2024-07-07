package g.sig.questweaver.data.repositories

import g.sig.questweaver.data.datasources.game.GameDataSource
import g.sig.questweaver.data.datasources.recentgames.RecentGamesDataSource
import g.sig.questweaver.data.mapper.toDomain
import g.sig.questweaver.data.mapper.toDto
import g.sig.questweaver.domain.entities.common.Game
import g.sig.questweaver.domain.repositories.GameRepository

class GameRepositoryImpl(
    private val dataSource: GameDataSource,
    private val recentGamesDataSource: RecentGamesDataSource,
) : GameRepository {
    override suspend fun getGame(gameId: String?): Game? =
        gameId?.let { recentGamesDataSource.getGame(it)?.toDomain() }
            ?: dataSource.currentGame.value?.toDomain()

    override suspend fun removeGame(gameId: String?) {
        if (gameId == null) return
        dataSource.deleteGame(gameId)
        recentGamesDataSource.removeGame(gameId)
    }

    override suspend fun createGame(game: Game) {
        dataSource.createGame(game.toDto())
        recentGamesDataSource.saveGame(game.toDto())
    }

    override suspend fun updateGame(game: Game) {
        dataSource.updateGame(game.toDto())
        recentGamesDataSource.saveGame(game.toDto())
    }
}
