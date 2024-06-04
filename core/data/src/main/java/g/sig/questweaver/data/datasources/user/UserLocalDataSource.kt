package g.sig.questweaver.data.datasources.user

import android.content.Context
import g.sig.questweaver.data.entities.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class UserLocalDataSource(
    context: Context,
    private val ioDispatcher: CoroutineDispatcher
) : UserDataSource {
    private val userDataStore = UserDataStore(context)

    override suspend fun getUser(): User = withContext(ioDispatcher) {
        userDataStore.data.first()
    }

    override suspend fun saveUser(user: User) {
        withContext(ioDispatcher) {
            userDataStore.updateData { user }
        }
    }

    override suspend fun deleteUser() {
        withContext(ioDispatcher) {
            userDataStore.updateData { User.Empty }
        }
    }

    override suspend fun updateUser(user: User) {
        withContext(ioDispatcher) {
            userDataStore.updateData { user }
        }
    }

    override suspend fun hasUser(): Boolean {
        return getUser() != User.Empty
    }
}
