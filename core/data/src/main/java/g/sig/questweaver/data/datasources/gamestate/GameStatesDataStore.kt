package g.sig.questweaver.data.datasources.gamestate

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import g.sig.questweaver.data.dto.GameStatesDto
import kotlinx.coroutines.flow.Flow

class GameStatesDataStore(
    private val context: Context,
) : DataStore<GameStatesDto> {
    private val Context.dataStore by dataStore("gameState.pb", GameStatesSerializer)

    override val data: Flow<GameStatesDto> = context.dataStore.data

    override suspend fun updateData(transform: suspend (t: GameStatesDto) -> GameStatesDto): GameStatesDto =
        context.dataStore.updateData { transform(it) }
}
