package g.sig.questweaver.domain.entities.common

import g.sig.questweaver.domain.entities.DomainEntity
import g.sig.questweaver.domain.entities.blocks.Color
import g.sig.questweaver.domain.entities.blocks.Point
import g.sig.questweaver.domain.entities.blocks.Size
import java.util.UUID

sealed interface Interaction : DomainEntity {
    data class Drawing(
        val strokeWidth: Int,
        val color: Color,
        val path: List<Point>,
        val id: UUID = UUID.randomUUID()
    ) : Interaction {
//        val pointsToStream = points
//            .runningReduce { acc, point ->
//                if (acc.distanceTo(point) > POINT_TOLERANCE) point else acc
//            }
//            .distinctUntilChanged()
//
//        companion object {
//            private const val POINT_TOLERANCE = 0.1f
//        }
    }

    data class Text(
        val text: String,
        val size: Size,
        val color: Color,
        val anchor: Point,
        val id: UUID = UUID.randomUUID()
    ) : Interaction
}
