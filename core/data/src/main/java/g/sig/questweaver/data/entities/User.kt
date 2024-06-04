@file:UseSerializers(UUIDSerializer::class)

package g.sig.questweaver.data.entities

import g.sig.questweaver.common.data.serializers.UUIDSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.util.UUID
import g.sig.questweaver.domain.entities.User as DomainUser

@Serializable
data class User(val id: UUID, val name: String) : DataEntity {
    fun toDomain() = DomainUser(name, id.toString())

    companion object {
        val Empty = User(UUID.nameUUIDFromBytes("".toByteArray()), "")

        fun DomainUser.fromDomain() = User(UUID.fromString(id), name)
    }
}
