package g.sig.questweaver.domain.entities.common

import g.sig.questweaver.domain.entities.DomainEntity
import g.sig.questweaver.domain.entities.blocks.Point

data class TransformationData(
    val scale: Float = 1f,
    val anchor: Point = Point.Zero,
    val rotation: Float = 0f,
) : DomainEntity
