package g.sig.data.datasources.recentgames

import g.sig.data.entities.recentgames.RecentGame

interface RecentGamesDataSource {
    suspend fun getRecentGames(): List<RecentGame>
}