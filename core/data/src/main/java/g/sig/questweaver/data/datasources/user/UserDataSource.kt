package g.sig.questweaver.data.datasources.user

import g.sig.questweaver.data.entities.User

interface UserDataSource {
    suspend fun getUser(): User
    suspend fun saveUser(user: User)
    suspend fun deleteUser()
    suspend fun updateUser(user: User)
    suspend fun hasUser(): Boolean
}
