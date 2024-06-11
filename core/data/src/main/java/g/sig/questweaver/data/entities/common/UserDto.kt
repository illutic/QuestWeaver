@file:UseSerializers(UUIDSerializer::class)

package g.sig.questweaver.data.entities.common

import g.sig.questweaver.common.data.serializers.UUIDSerializer
import g.sig.questweaver.data.entities.Dto
import g.sig.questweaver.domain.entities.common.User
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.util.UUID

@Serializable
data class UserDto(val id: UUID, val name: String) : Dto {
    fun toDomain() = User(name, id.toString())

    companion object {
        val Empty = UserDto(UUID.nameUUIDFromBytes("".toByteArray()), "")

        fun User.fromDomain() = UserDto(UUID.fromString(id), name)
    }
}
