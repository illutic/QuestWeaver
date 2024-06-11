package g.sig.questweaver.data.datasources.recentgames

import g.sig.questweaver.data.entities.common.GameDto

interface RecentGamesDataSource {
    suspend fun getRecentGames(): List<GameDto>
    suspend fun getGame(gameId: String): GameDto?
    suspend fun saveGame(gameDto: GameDto)
}
