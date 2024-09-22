package g.sig.questweaver.game.home.state

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import g.sig.questweaver.common.ui.mappers.toColor
import g.sig.questweaver.common.ui.mappers.topLeft
import g.sig.questweaver.domain.entities.DomainEntity
import g.sig.questweaver.domain.entities.blocks.Point
import g.sig.questweaver.domain.entities.blocks.Size
import g.sig.questweaver.domain.entities.blocks.optimize
import g.sig.questweaver.domain.entities.common.Annotation
import g.sig.questweaver.domain.entities.common.RemoveAnnotation
import g.sig.questweaver.domain.entities.common.TransformationData
import g.sig.questweaver.domain.entities.common.User
import java.util.UUID

@Stable
data class GameHomeState(
    val gameId: String = "",
    val user: User = User.Empty,
    val isLoading: Boolean = false,
    val allowAnnotations: Boolean = true,
    val isDM: Boolean = false,
    val annotationMode: AnnotationMode = AnnotationMode.Idle,
    val annotations: Map<String, Annotation> = emptyMap(),
    val selectedColor: Color = Color.Black,
    val opacity: Float = selectedColor.alpha,
    val selectedSize: Size = Size.Default,
    val selectedPlayer: User? = null,
    val users: List<User> = emptyList(),
    val showColorPicker: Boolean = false,
) {
    fun makeDrawing(path: List<Point>): Annotation.Drawing? {
        if (opacity < ALPHA_MIN) return null
        val points = path.optimize()
        return Annotation.Drawing(
            path = points,
            strokeSize = selectedSize,
            color = selectedColor.toColor(),
            createdBy = user.id,
            id = UUID.randomUUID().toString(),
            transformationData = TransformationData(anchor = points.topLeft()),
        )
    }

    fun makeText(anchor: Point): Annotation.Text? {
        if (opacity < ALPHA_MIN) return null
        return Annotation.Text(
            text = "",
            color = selectedColor.toColor(),
            createdBy = user.id,
            id = UUID.randomUUID().toString(),
            transformationData = TransformationData(anchor = anchor),
        )
    }

    fun getUpdatedText(
        id: String,
        text: String,
    ): DomainEntity? {
        val textAnnotation = annotations[id] as? Annotation.Text ?: return null
        val updatedText = textAnnotation.copy(text = text)

        return if (updatedText.text.isEmpty()) {
            RemoveAnnotation(updatedText.id)
        } else {
            updatedText
        }
    }

    fun updateTransformation(
        id: String,
        scale: Float,
        rotation: Float,
        anchor: Point,
    ): Annotation? {
        val annotation = annotations[id] ?: return null
        val transformationData =
            TransformationData(scale = scale, rotation = rotation, anchor = anchor)
        val updatedAnnotation =
            when (annotation) {
                is Annotation.Drawing -> annotation.copy(transformationData = transformationData)
                is Annotation.Image -> annotation.copy(transformationData = transformationData)
                is Annotation.Text -> annotation.copy(transformationData = transformationData)
            }
        return updatedAnnotation
    }

    fun updateOpacity(opacity: Float): GameHomeState =
        copy(
            selectedColor = selectedColor.copy(alpha = opacity),
            opacity = opacity,
        )

    fun updateSize(size: Float): GameHomeState = copy(selectedSize = Size(size, size))

    companion object {
        private const val ALPHA_MIN = 0.1f
    }
}

enum class AnnotationMode {
    Idle,
    RemoveMode,
    DrawingMode,
    TextMode,
    DMMode,
}
