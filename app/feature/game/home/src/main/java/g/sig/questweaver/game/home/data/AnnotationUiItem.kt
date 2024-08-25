package g.sig.questweaver.game.home.data

import android.net.Uri
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.core.net.toUri
import g.sig.questweaver.common.ui.mappers.getStrokeWidth
import g.sig.questweaver.common.ui.mappers.toComposeColor
import g.sig.questweaver.common.ui.mappers.toOffset
import g.sig.questweaver.domain.entities.common.Annotation
import g.sig.questweaver.domain.entities.common.TransformationData

data class TransformationDataUiItem(
    val scale: Float,
    val anchor: Offset,
    val rotation: Float,
)

sealed interface AnnotationUiItem {
    val id: String
    val createdBy: String
    val transformationData: TransformationDataUiItem
}

data class Drawing(
    override val id: String,
    override val createdBy: String,
    override val transformationData: TransformationDataUiItem,
    val stroke: Stroke,
    val color: Color,
    val path: List<Offset>,
) : AnnotationUiItem

data class Text(
    override val id: String,
    override val createdBy: String,
    override val transformationData: TransformationDataUiItem,
    val text: String,
    val color: Color,
) : AnnotationUiItem

data class Image(
    override val id: String,
    override val createdBy: String,
    override val transformationData: TransformationDataUiItem,
    val uri: Uri,
    val width: Float,
    val height: Float,
) : AnnotationUiItem

fun TransformationData.toUiItem(canvasSize: Size): TransformationDataUiItem =
    TransformationDataUiItem(
        scale = scale,
        anchor = anchor.toOffset(canvasSize),
        rotation = rotation,
    )

fun Annotation.toUiItem(canvasSize: Size): AnnotationUiItem =
    when (this) {
        is Annotation.Drawing ->
            Drawing(
                id = id,
                createdBy = createdBy,
                stroke = strokeSize.getStrokeWidth(canvasSize),
                color = color.toComposeColor(),
                path = path.map { it.toOffset(canvasSize) },
                transformationData = transformationData.toUiItem(canvasSize),
            )

        is Annotation.Text ->
            Text(
                id = id,
                createdBy = createdBy,
                text = text,
                color = color.toComposeColor(),
                transformationData = transformationData.toUiItem(canvasSize),
            )

        is Annotation.Image ->
            Image(
                id = id,
                createdBy = createdBy,
                uri = uri.value.toUri(),
                transformationData = transformationData.toUiItem(canvasSize),
                width = width,
                height = height,
            )
    }
