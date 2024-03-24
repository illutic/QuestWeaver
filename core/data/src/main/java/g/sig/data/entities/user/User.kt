@file:UseSerializers(UUIDSerializer::class)

package g.sig.data.entities.user

import g.sig.data.serializers.UUIDSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.util.UUID

@Serializable
data class User(val id: UUID, val name: String) {
    companion object {
        val Empty = User(UUID.nameUUIDFromBytes("".toByteArray()), "")
    }
}
