package g.sig.data.repositories

import g.sig.data.datasources.user.UserDataSource
import g.sig.data.entities.user.User

class UserRepository(
    private val userDataSource: UserDataSource
) {
    suspend fun getUser() = userDataSource.getUser()
    suspend fun saveUser(user: User) = userDataSource.saveUser(user)
    suspend fun deleteUser() = userDataSource.deleteUser()
    suspend fun updateUser(user: User) = userDataSource.updateUser(user)
    suspend fun hasUser() = userDataSource.hasUser()

}