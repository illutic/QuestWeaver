package g.sig.data.repositories

import g.sig.domain.datasource.UserDataSource
import g.sig.domain.entities.User
import g.sig.domain.repositories.UserRepository

class UserRepositoryImpl(
    private val userDataSource: UserDataSource
) : UserRepository {
    override suspend fun getUser() = userDataSource.getUser()
    override suspend fun saveUser(user: User) = userDataSource.saveUser(user)
    override suspend fun deleteUser() = userDataSource.deleteUser()
    override suspend fun updateUser(user: User) = userDataSource.updateUser(user)
    override suspend fun hasUser() = userDataSource.hasUser()

}