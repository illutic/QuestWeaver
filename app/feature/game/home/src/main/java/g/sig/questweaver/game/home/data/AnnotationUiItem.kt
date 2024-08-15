package g.sig.questweaver.game.home.data

import android.net.Uri
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.core.net.toUri
import g.sig.questweaver.common.ui.mappers.getStrokeWidth
import g.sig.questweaver.common.ui.mappers.toComposeColor
import g.sig.questweaver.common.ui.mappers.toComposeSize
import g.sig.questweaver.common.ui.mappers.toOffset
import g.sig.questweaver.domain.entities.common.Annotation

sealed interface AnnotationUiItem {
    val id: String
    val canEdit: Boolean
}

data class Drawing(
    override val id: String,
    override val canEdit: Boolean,
    val stroke: Stroke,
    val color: Color,
    val path: List<Offset>,
) : AnnotationUiItem

data class Text(
    override val id: String,
    override val canEdit: Boolean,
    val text: String,
    val size: Size,
    val color: Color,
    val anchor: Offset,
) : AnnotationUiItem

data class Image(
    override val id: String,
    override val canEdit: Boolean,
    val uri: Uri,
    val size: Size,
    val anchor: Offset,
) : AnnotationUiItem

fun Annotation.asUiItem(
    canvasSize: Size,
    userId: String,
): AnnotationUiItem =
    when (this) {
        is Annotation.Drawing ->
            Drawing(
                id = id,
                canEdit = createdBy == userId,
                stroke = strokeSize.getStrokeWidth(canvasSize),
                color = color.toComposeColor(),
                path = path.map { it.toOffset(canvasSize) },
            )

        is Annotation.Text ->
            Text(
                id = id,
                canEdit = createdBy == userId,
                text = text,
                size = size.toComposeSize(canvasSize),
                color = color.toComposeColor(),
                anchor = anchor.toOffset(canvasSize),
            )

        is Annotation.Image ->
            Image(
                id = id,
                canEdit = createdBy == userId,
                uri = uri.value.toUri(),
                size = size.toComposeSize(canvasSize),
                anchor = anchor.toOffset(canvasSize),
            )
    }
