package g.sig.questweaver.data.datasources.recentgames

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import g.sig.questweaver.data.dto.GameDto
import g.sig.questweaver.data.dto.GamesDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RecentGamesDataStore(private val context: Context) : DataStore<List<GameDto>> {
    private val Context.dataStore by dataStore("recentGame.pb", RecentGameSerializer)

    override val data: Flow<List<GameDto>> = context.dataStore.data.map { it.gameDtos }

    override suspend fun updateData(transform: suspend (t: List<GameDto>) -> List<GameDto>): List<GameDto> =
        context.dataStore.updateData { GamesDto(transform(it.gameDtos)) }.gameDtos
}
