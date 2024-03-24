package g.sig.data.datasources.user

import android.content.Context
import g.sig.data.datastore.UserDataStore
import g.sig.data.entities.user.User.Companion.fromDomain
import g.sig.domain.datasource.UserDataSource
import g.sig.domain.entities.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import g.sig.data.entities.user.User as DataUser

class UserLocalDataSource(
    context: Context,
    private val ioDispatcher: CoroutineDispatcher
) : UserDataSource {
    private val userDataStore = UserDataStore(context)

    override suspend fun getUser(): User = withContext(ioDispatcher) {
        userDataStore.data.first().toDomain()
    }

    override suspend fun saveUser(user: User) {
        withContext(ioDispatcher) {
            userDataStore.updateData { user.fromDomain() }
        }
    }

    override suspend fun deleteUser() {
        withContext(ioDispatcher) {
            userDataStore.updateData { DataUser.Empty }
        }
    }

    override suspend fun updateUser(user: User) {
        withContext(ioDispatcher) {
            userDataStore.updateData { user.fromDomain() }
        }
    }

    override suspend fun hasUser(): Boolean {
        return getUser().fromDomain() != DataUser.Empty
    }
}