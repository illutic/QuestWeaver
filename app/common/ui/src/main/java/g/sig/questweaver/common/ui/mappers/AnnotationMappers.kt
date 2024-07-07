package g.sig.questweaver.common.ui.mappers

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import g.sig.questweaver.domain.entities.common.Annotation

fun Annotation.getBounds(canvasSize: Size) =
    when (this) {
        is Annotation.Drawing -> path.getBounds(canvasSize)
        is Annotation.Text ->
            Rect(
                topLeft = anchor.toOffset(canvasSize),
                bottomRight = anchor.toOffset(canvasSize) + size.toOffset(canvasSize),
            )

        is Annotation.Image ->
            Rect(
                topLeft = anchor.toOffset(canvasSize),
                bottomRight = anchor.toOffset(canvasSize) + size.toOffset(canvasSize),
            )
    }

fun Iterable<Annotation>.getClickedAnnotation(
    touchPoint: Offset,
    canvasSize: Size,
): Annotation? = firstOrNull { annotation -> annotation.getBounds(canvasSize).contains(touchPoint) }
