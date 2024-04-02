package g.sig.data.datasources.recentgames

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import g.sig.data.entities.Game
import g.sig.data.entities.Games
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RecentGamesDataStore(private val context: Context) : DataStore<List<Game>> {
    private val Context.dataStore by dataStore("recentGame.pb", RecentGameSerializer)

    override val data: Flow<List<Game>> = context.dataStore.data.map { it.games }

    override suspend fun updateData(transform: suspend (t: List<Game>) -> List<Game>): List<Game> =
        context.dataStore.updateData { Games(transform(it.games)) }.games
}