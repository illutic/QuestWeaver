package g.sig.questweaver.data.entities.blocks

import g.sig.questweaver.data.entities.Dto
import g.sig.questweaver.domain.entities.blocks.Size
import kotlinx.serialization.Serializable

@Serializable
data class SizeDto(val width: Float, val height: Float) : Dto {
    fun toDomain() = Size(width, height)

    companion object {
        fun Size.fromDomain() = SizeDto(width, height)
    }
}
