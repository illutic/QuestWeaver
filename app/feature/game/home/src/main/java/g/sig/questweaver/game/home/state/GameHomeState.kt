package g.sig.questweaver.game.home.state

import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import g.sig.questweaver.domain.entities.blocks.Size
import g.sig.questweaver.domain.entities.common.Annotation
import g.sig.questweaver.domain.entities.common.User

@Stable
class GameHomeState {
    var allowAnnotations by mutableStateOf(true)
    var isDM by mutableStateOf(false)
    var annotationMode by mutableStateOf(AnnotationMode.Idle)
    var annotations = mutableStateListOf<Annotation>()
    var selectedColor by mutableStateOf(Color.Black)
    val opacity by derivedStateOf { selectedColor.alpha }
    var selectedSize by mutableStateOf(Size.Default)
    var selectedAnnotation: Annotation? by mutableStateOf(null)
    var selectedText by mutableStateOf("")
    var selectedPlayer: User? by mutableStateOf(null)
    var users: List<User> by mutableStateOf(emptyList())
    var showColorPicker by mutableStateOf(false)

    enum class AnnotationMode {
        Idle,
        RemoveMode,
        DrawingMode,
        TextMode,
        DMMode
    }
}
