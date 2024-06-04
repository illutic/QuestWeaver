package g.sig.questweaver.data.datasources.user

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import g.sig.questweaver.data.entities.User
import kotlinx.coroutines.flow.Flow

class UserDataStore(private val context: Context) : DataStore<User> {
    private val Context.dataStore by dataStore("user.pb", UserSerializer)

    override val data: Flow<User> = context.dataStore.data

    override suspend fun updateData(transform: suspend (t: User) -> User): User =
        context.dataStore.updateData(transform)
}
