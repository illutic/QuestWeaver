package g.sig.questweaver.domain.entities.blocks

import g.sig.questweaver.domain.entities.DomainEntity
import kotlin.math.sqrt

data class Point(
    val x: Float,
    val y: Float,
) : DomainEntity {
    init {
        require(x in 0f..1f) { "x must be in [0, 1], but was $x" }
        require(y in 0f..1f) { "y must be in [0, 1], but was $y" }
    }

    fun distanceTo(other: Point): Float {
        val dx = x - other.x
        val dy = y - other.y
        return sqrt((dx * dx + dy * dy).toDouble()).toFloat()
    }

    companion object {
        val Zero = Point(0f, 0f)
    }
}

fun List<Point>.optimize(): List<Point> =
    runningReduce { acc, point ->
        if (acc.distanceTo(point) > POINT_TOLERANCE) point else acc
    }

private const val POINT_TOLERANCE = 0.01f
