package g.sig.questweaver.domain.repositories

import g.sig.questweaver.domain.entities.User

interface UserRepository {
    suspend fun getUser(): User
    suspend fun saveUser(user: User)
    suspend fun deleteUser()
    suspend fun updateUser(user: User)
    suspend fun hasUser(): Boolean
}
