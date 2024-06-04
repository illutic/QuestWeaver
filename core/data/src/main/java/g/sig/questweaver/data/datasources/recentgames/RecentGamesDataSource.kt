package g.sig.questweaver.data.datasources.recentgames

import g.sig.questweaver.data.entities.Game

interface RecentGamesDataSource {
    suspend fun getRecentGames(): List<Game>
    suspend fun getGame(gameId: String): Game?
    suspend fun saveGame(game: Game)
}
