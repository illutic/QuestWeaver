package g.sig.data.datasources.recentgames

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import g.sig.data.entities.recentgames.RecentGame
import kotlinx.coroutines.flow.Flow

class RecentGamesDataStore(private val context: Context) : DataStore<RecentGame> {
    private val Context.dataStore by dataStore("recentGame.pb", RecentGameSerializer)

    override val data: Flow<RecentGame> = context.dataStore.data

    override suspend fun updateData(transform: suspend (t: RecentGame) -> RecentGame): RecentGame =
        context.dataStore.updateData(transform)
}