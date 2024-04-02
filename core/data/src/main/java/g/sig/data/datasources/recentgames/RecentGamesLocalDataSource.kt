package g.sig.data.datasources.recentgames

import android.content.Context
import g.sig.data.entities.Game
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
}