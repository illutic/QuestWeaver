@file:UseSerializers(UUIDSerializer::class)

package g.sig.data.entities

import g.sig.data.serializers.UUIDSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.util.UUID
import g.sig.domain.entities.User as DomainUser

@Serializable
data class User(val id: UUID, val name: String) {
    fun toDomain() = DomainUser(name, id.toString())

    companion object {
        val Empty = User(UUID.nameUUIDFromBytes("".toByteArray()), "")

        fun DomainUser.fromDomain() = User(UUID.fromString(id), name)
    }
}
