package g.sig.data.repositories

import g.sig.data.datasources.recentgames.RecentGamesDataSource
import g.sig.domain.entities.Game
import g.sig.domain.repositories.RecentGamesRepository

class RecentGamesRepositoryImpl(
    private val dataSource: RecentGamesDataSource
) : RecentGamesRepository {
    override suspend fun getRecentGames(): List<Game> =
        dataSource.getRecentGames().map { it.toDomain() }
}