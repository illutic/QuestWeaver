package g.sig.questweaver.data.repositories

import g.sig.questweaver.data.datasources.user.UserDataSource
import g.sig.questweaver.data.mapper.toDomain
import g.sig.questweaver.data.mapper.toDto
import g.sig.questweaver.domain.entities.common.User
import g.sig.questweaver.domain.repositories.UserRepository

class UserRepositoryImpl(
    private val userDataSource: UserDataSource,
) : UserRepository {
    override suspend fun getUser() = userDataSource.getUser().toDomain()

    override suspend fun saveUser(user: User) = userDataSource.saveUser(user.toDto())

    override suspend fun deleteUser() = userDataSource.deleteUser()

    override suspend fun updateUser(user: User) = userDataSource.updateUser(user.toDto())

    override suspend fun hasUser() = userDataSource.hasUser()
}
