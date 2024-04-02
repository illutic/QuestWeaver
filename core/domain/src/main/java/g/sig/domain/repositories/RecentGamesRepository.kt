package g.sig.domain.repositories

import g.sig.domain.entities.Game

interface RecentGamesRepository {
    suspend fun getRecentGames(): List<Game>
}