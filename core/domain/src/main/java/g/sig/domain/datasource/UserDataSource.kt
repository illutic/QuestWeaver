package g.sig.domain.datasource

import g.sig.domain.entities.User

interface UserDataSource {
    suspend fun getUser(): User
    suspend fun saveUser(user: User)
    suspend fun deleteUser()
    suspend fun updateUser(user: User)
    suspend fun hasUser(): Boolean
}