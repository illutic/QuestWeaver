package g.sig.questweaver.data.entities.blocks

import g.sig.questweaver.data.entities.Dto
import g.sig.questweaver.domain.entities.blocks.Color
import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class ColorDto(val value: UInt) : Dto {
    fun toDomain() = Color(value)

    companion object {
        fun Color.fromDomain() = ColorDto(value)
    }
}
