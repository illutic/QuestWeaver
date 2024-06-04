package g.sig.questweaver.data.datasources.recentgames

import android.content.Context
import g.sig.questweaver.data.entities.Game
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class RecentGamesLocalDataSource(
    context: Context,
    private val ioDispatcher: CoroutineDispatcher
) : RecentGamesDataSource {
    private val recentGamesDataStore = RecentGamesDataStore(context)

    override suspend fun getRecentGames(): List<Game> =
        withContext(ioDispatcher) { recentGamesDataStore.data.first() }

    override suspend fun getGame(gameId: String): Game? =
        getRecentGames().firstOrNull { it.gameId == gameId }

    override suspend fun saveGame(game: Game): Unit = withContext(ioDispatcher) {
        recentGamesDataStore.updateData { recentGames ->
            recentGames.toMutableList().apply {
                removeIf { recentGame -> recentGame.gameId == game.gameId }
                add(0, game)
            }
        }
    }
}
