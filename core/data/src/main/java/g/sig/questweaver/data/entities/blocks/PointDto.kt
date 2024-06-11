package g.sig.questweaver.data.entities.blocks

import g.sig.questweaver.data.entities.Dto
import g.sig.questweaver.domain.entities.blocks.Point
import kotlinx.serialization.Serializable

@Serializable
data class PointDto(val x: Float, val y: Float) : Dto {
    fun toDomain() = Point(x, y)

    companion object {
        fun Point.fromDomain() = PointDto(x, y)
    }
}
