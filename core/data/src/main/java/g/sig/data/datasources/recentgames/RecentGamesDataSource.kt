package g.sig.data.datasources.recentgames

import g.sig.data.entities.Game

interface RecentGamesDataSource {
    suspend fun getRecentGames(): List<Game>
    suspend fun getGame(gameId: String): Game?
}