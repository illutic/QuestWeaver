package g.sig.questweaver.data.datasources.recentgames

import android.content.Context
import g.sig.questweaver.data.dto.GameDto
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class RecentGamesLocalDataSource(
    context: Context,
    private val ioDispatcher: CoroutineDispatcher
) : RecentGamesDataSource {
    private val recentGamesDataStore = RecentGamesDataStore(context)

    override suspend fun getRecentGames(): List<GameDto> =
        withContext(ioDispatcher) { recentGamesDataStore.data.first() }

    override suspend fun getGame(gameId: String): GameDto? =
        getRecentGames().firstOrNull { it.gameId == gameId }

    override suspend fun saveGame(gameDto: GameDto): Unit = withContext(ioDispatcher) {
        recentGamesDataStore.updateData { recentGames ->
            recentGames.toMutableList().apply {
                removeIf { recentGame -> recentGame.gameId == gameDto.gameId }
                add(0, gameDto)
            }
        }
    }
}
