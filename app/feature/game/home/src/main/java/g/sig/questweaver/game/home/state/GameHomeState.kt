package g.sig.questweaver.game.home.state

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import g.sig.questweaver.domain.entities.blocks.Size
import g.sig.questweaver.domain.entities.common.Annotation
import g.sig.questweaver.domain.entities.common.User

@Stable
data class GameHomeState(
    val allowAnnotations: Boolean = true,
    val isDM: Boolean = false,
    val annotationMode: AnnotationMode = AnnotationMode.Idle,
    val annotations: Map<String, Annotation> = emptyMap(),
    val selectedColor: Color = Color.Black,
    val opacity: Float = selectedColor.alpha,
    val selectedSize: Size = Size.Default,
    val selectedAnnotation: Annotation? = null,
    val selectedText: String = "",
    val selectedPlayer: User? = null,
    val users: List<User> = emptyList(),
    val showColorPicker: Boolean = false,
) {
    enum class AnnotationMode {
        Idle,
        RemoveMode,
        DrawingMode,
        TextMode,
        DMMode,
    }
}
