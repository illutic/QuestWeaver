package g.sig.questweaver.common.ui.mappers

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.toRect
import g.sig.questweaver.domain.entities.common.Annotation

fun Annotation.getBounds(canvasSize: Size) = when (this) {
    is Annotation.Drawing -> path.getBounds(canvasSize)
    is Annotation.Text -> Size(
        width = size.width * canvasSize.width + anchor.x * canvasSize.width,
        height = size.height * canvasSize.height + anchor.y * canvasSize.height
    ).toRect()

    is Annotation.Image -> Size(
        width = size.width * canvasSize.width + anchor.x * canvasSize.width,
        height = size.height * canvasSize.height + anchor.y * canvasSize.height
    ).toRect()
}

fun Iterable<Annotation>.getClickedAnnotation(touchPoint: Offset, canvasSize: Size): Annotation? =
    firstOrNull { annotation -> annotation.getBounds(canvasSize).contains(touchPoint) }
