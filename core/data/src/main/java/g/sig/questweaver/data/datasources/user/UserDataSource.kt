package g.sig.questweaver.data.datasources.user

import g.sig.questweaver.data.dto.UserDto

interface UserDataSource {
    suspend fun getUser(): UserDto

    suspend fun saveUser(userDto: UserDto)

    suspend fun deleteUser()

    suspend fun updateUser(userDto: UserDto)

    suspend fun hasUser(): Boolean
}
