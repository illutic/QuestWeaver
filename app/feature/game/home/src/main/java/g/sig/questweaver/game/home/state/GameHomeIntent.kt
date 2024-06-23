package g.sig.questweaver.game.home.state

import androidx.compose.ui.graphics.Color
import g.sig.questweaver.domain.entities.blocks.Point
import g.sig.questweaver.domain.entities.blocks.Size
import g.sig.questweaver.domain.entities.common.Annotation
import g.sig.questweaver.domain.entities.common.User

sealed interface GameHomeIntent {
    data object Back : GameHomeIntent
    data object Load : GameHomeIntent

    data class ChangeMode(val mode: GameHomeState.AnnotationMode) : GameHomeIntent
    data class AddText(val text: String, val size: Size, val anchor: Point) : GameHomeIntent
    data class AddDrawing(val path: List<Point>, val strokeSize: Size) : GameHomeIntent
    data class AddImage(val uri: String) : GameHomeIntent
    data class SelectColor(val color: Color) : GameHomeIntent
    data class SelectOpacity(val opacity: Float) : GameHomeIntent
    data class SelectSize(val size: Float) : GameHomeIntent
    data class SelectAnnotation(val annotation: Annotation?) : GameHomeIntent
    data class SelectPlayer(val player: User) : GameHomeIntent
    data object ShowColorPicker : GameHomeIntent
}
