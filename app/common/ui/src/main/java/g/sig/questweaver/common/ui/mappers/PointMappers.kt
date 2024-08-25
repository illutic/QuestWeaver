package g.sig.questweaver.common.ui.mappers

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import g.sig.questweaver.domain.entities.blocks.Point

fun Point.toOffset(canvasSize: Size) = Offset(x * canvasSize.width, y * canvasSize.height)

fun Offset.toPoint(canvasSize: Size) =
    with(canvasSize) {
        Point(x.coerceIn(0f, width) / width, y.coerceIn(0f, height) / height)
    }

fun List<Point>.toPath(canvasSize: Size) =
    Path().apply {
        if (isEmpty()) return@apply

        moveTo(first().x * canvasSize.width, first().y * canvasSize.height)
        for (point in drop(1)) {
            lineTo(point.x * canvasSize.width, point.y * canvasSize.height)
        }
    }

fun List<Point>.topLeft() =
    if (isEmpty()) {
        Point.Zero
    } else {
        Point(
            x = minOf { it.x },
            y = minOf { it.y },
        )
    }

fun List<Offset>.toPath() =
    Path().apply {
        if (isEmpty()) return@apply
        val topLeftX = minOf { it.x }
        val topLeftY = minOf { it.y }

        moveTo(first().x - topLeftX, first().y - topLeftY)
        for (point in drop(1)) {
            lineTo(point.x - topLeftX, point.y - topLeftY)
        }
    }
