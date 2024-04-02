package g.sig.data.repositories

import g.sig.data.datasources.user.UserDataSource
import g.sig.data.entities.User.Companion.fromDomain
import g.sig.domain.entities.User
import g.sig.domain.repositories.UserRepository

class UserRepositoryImpl(
    private val userDataSource: UserDataSource
) : UserRepository {
    override suspend fun getUser() = userDataSource.getUser().toDomain()
    override suspend fun saveUser(user: User) = userDataSource.saveUser(user.fromDomain())
    override suspend fun deleteUser() = userDataSource.deleteUser()
    override suspend fun updateUser(user: User) = userDataSource.updateUser(user.fromDomain())
    override suspend fun hasUser() = userDataSource.hasUser()

}