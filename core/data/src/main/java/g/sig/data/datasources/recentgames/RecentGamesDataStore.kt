package g.sig.data.datasources.recentgames

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import g.sig.data.entities.recentgames.RecentGame
import g.sig.data.entities.recentgames.RecentGames
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RecentGamesDataStore(private val context: Context) : DataStore<List<RecentGame>> {
    private val Context.dataStore by dataStore("recentGame.pb", RecentGameSerializer)

    override val data: Flow<List<RecentGame>> = context.dataStore.data.map { it.games }

    override suspend fun updateData(transform: suspend (t: List<RecentGame>) -> List<RecentGame>): List<RecentGame> =
        context.dataStore.updateData { RecentGames(transform(it.games)) }.games
}