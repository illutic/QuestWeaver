package g.sig.questweaver.game.home.state

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import g.sig.questweaver.common.ui.mappers.toColor
import g.sig.questweaver.common.ui.mappers.topLeft
import g.sig.questweaver.domain.entities.blocks.Point
import g.sig.questweaver.domain.entities.blocks.Size
import g.sig.questweaver.domain.entities.blocks.optimize
import g.sig.questweaver.domain.entities.common.Annotation
import g.sig.questweaver.domain.entities.common.TransformationData
import g.sig.questweaver.domain.entities.common.User
import g.sig.questweaver.domain.entities.states.GameState
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
    private fun addAnnotation(annotation: Annotation): GameHomeState {
        if (opacity < ALPHA_MIN) return this
        val updatedAnnotations = annotations.toMutableMap()
        updatedAnnotations[annotation.id] = annotation
        return copy(annotations = updatedAnnotations)
    }

    private fun updateAnnotation(annotation: Annotation): GameHomeState {
        val updatedAnnotations = annotations.toMutableMap()
        updatedAnnotations[annotation.id] = annotation
        return copy(annotations = updatedAnnotations)
    }

    private fun removeAnnotation(annotation: Annotation): GameHomeState {
        val updatedAnnotations = annotations.toMutableMap()
        updatedAnnotations.remove(annotation.id)
        return copy(annotations = updatedAnnotations)
    }

    fun addDrawing(path: List<Point>): GameHomeState {
        val points = path.optimize()
        val drawing =
            Annotation.Drawing(
                path = points,
                strokeSize = selectedSize,
                color = selectedColor.toColor(),
                createdBy = user.id,
                id = UUID.randomUUID().toString(),
                transformationData = TransformationData(anchor = points.topLeft()),
            )

        return addAnnotation(drawing)
    }

    fun addText(anchor: Point): GameHomeState {
        if (opacity < ALPHA_MIN) return this
        val text =
            Annotation.Text(
                text = "",
                color = selectedColor.toColor(),
                createdBy = user.id,
                id = UUID.randomUUID().toString(),
                transformationData = TransformationData(anchor = anchor),
            )
        return addAnnotation(text)
    }

    fun updateText(
        id: String,
        text: String,
    ): GameHomeState {
        val textAnnotation = annotations[id] as? Annotation.Text ?: return this
        val updatedText = textAnnotation.copy(text = text)

        return if (updatedText.text.isEmpty()) {
            removeAnnotation(updatedText)
        } else {
            updateAnnotation(updatedText)
        }
    }

    fun updateTransformation(
        id: String,
        scale: Float,
        rotation: Float,
        anchor: Point,
    ): GameHomeState {
        val annotation = annotations[id] ?: return this
        val transformationData =
            TransformationData(scale = scale, rotation = rotation, anchor = anchor)
        val updatedAnnotation =
            when (annotation) {
                is Annotation.Drawing -> annotation.copy(transformationData = transformationData)
                is Annotation.Image -> annotation.copy(transformationData = transformationData)
                is Annotation.Text -> annotation.copy(transformationData = transformationData)
            }
        return updateAnnotation(updatedAnnotation)
    }

    fun selectAnnotation(annotation: Annotation): GameHomeState {
        if (annotationMode != AnnotationMode.RemoveMode) return this
        return removeAnnotation(annotation)
    }

    fun updateOpacity(opacity: Float): GameHomeState =
        copy(
            selectedColor = selectedColor.copy(alpha = opacity),
            opacity = opacity,
        )

    fun updateSize(size: Float): GameHomeState = copy(selectedSize = Size(size, size))

    fun toGameState(): GameState =
        GameState(
            gameId = gameId,
            connectedUsers = users,
            annotations = annotations.values.toList(),
            allowEditing = allowAnnotations,
        )

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
