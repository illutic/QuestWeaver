package g.sig.questweaver.data.datasources.user

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import g.sig.questweaver.data.entities.common.UserDto
import kotlinx.coroutines.flow.Flow

class UserDataStore(private val context: Context) : DataStore<UserDto> {
    private val Context.dataStore by dataStore("user.pb", UserSerializer)

    override val data: Flow<UserDto> = context.dataStore.data

    override suspend fun updateData(transform: suspend (t: UserDto) -> UserDto): UserDto =
        context.dataStore.updateData(transform)
}
