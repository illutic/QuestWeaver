package g.sig.data.datasources.recentgames

import android.content.Context
import g.sig.data.entities.recentgames.RecentGame
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.withContext

class RecentGamesLocalDataSource(
    context: Context,
    private val ioDispatcher: CoroutineDispatcher
) : RecentGamesDataSource {
    private val recentGamesDataStore = RecentGamesDataStore(context)

    override suspend fun getRecentGames(): List<RecentGame> =
        withContext(ioDispatcher) { recentGamesDataStore.data.toList() }
}