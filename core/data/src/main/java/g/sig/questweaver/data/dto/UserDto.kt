@file:UseSerializers(UUIDSerializer::class)

package g.sig.questweaver.data.dto

import g.sig.questweaver.common.data.serializers.UUIDSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.util.UUID

@Serializable
data class UserDto(val id: UUID, val name: String) : Dto {
    companion object {
        val Empty = UserDto(UUID.nameUUIDFromBytes("".toByteArray()), "")
    }
}
