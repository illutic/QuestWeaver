package g.sig.questweaver.common.ui.mappers

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import g.sig.questweaver.domain.entities.blocks.Point

fun Point.toOffset(canvasSize: Size) = Offset(x * canvasSize.width, y * canvasSize.height)

fun Offset.toPoint(canvasSize: Size) = with(canvasSize) {
    Point(x.coerceIn(0f, width) / width, y.coerceIn(0f, height) / height)
}

fun List<Point>.toPath(canvasSize: Size) = Path().apply {
    if (isEmpty()) return@apply

    moveTo(first().x * canvasSize.width, first().y * canvasSize.height)
    for (point in drop(1)) {
        lineTo(point.x * canvasSize.width, point.y * canvasSize.height)
    }
}

fun List<Point>.getBounds(canvasSize: Size) = if (isEmpty()) Rect.Zero else {
    var left = 1f
    var top = 1f
    var right = 0f
    var bottom = 0f

    for (point in this) {
        left = minOf(left, point.x)
        top = minOf(top, point.y)
        right = maxOf(right, point.x)
        bottom = maxOf(bottom, point.y)
    }

    Rect(
        left = left * canvasSize.width,
        top = top * canvasSize.height,
        right = right * canvasSize.width,
        bottom = bottom * canvasSize.height
    )
}
