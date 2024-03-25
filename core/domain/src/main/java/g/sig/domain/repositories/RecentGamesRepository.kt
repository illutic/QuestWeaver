package g.sig.domain.repositories

import g.sig.domain.entities.RecentGame

interface RecentGamesRepository {
    suspend fun getRecentGames(): List<RecentGame>
}