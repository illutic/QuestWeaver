package g.sig.questweaver.data.datasources.user

import android.content.Context
import g.sig.questweaver.data.dto.UserDto
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class UserLocalDataSource(
    context: Context,
    private val ioDispatcher: CoroutineDispatcher
) : UserDataSource {
    private val userDataStore = UserDataStore(context)

    override suspend fun getUser(): UserDto = withContext(ioDispatcher) {
        userDataStore.data.first()
    }

    override suspend fun saveUser(userDto: UserDto) {
        withContext(ioDispatcher) {
            userDataStore.updateData { userDto }
        }
    }

    override suspend fun deleteUser() {
        withContext(ioDispatcher) {
            userDataStore.updateData { UserDto.Empty }
        }
    }

    override suspend fun updateUser(userDto: UserDto) {
        withContext(ioDispatcher) {
            userDataStore.updateData { userDto }
        }
    }

    override suspend fun hasUser(): Boolean {
        return getUser() != UserDto.Empty
    }
}
