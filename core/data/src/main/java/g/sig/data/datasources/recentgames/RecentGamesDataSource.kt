package g.sig.data.datasources.recentgames

import g.sig.data.entities.Game

interface RecentGamesDataSource {
    suspend fun getRecentGames(): List<Game>
}