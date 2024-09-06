package g.sig.questweaver.game.home.state

import androidx.compose.ui.graphics.Color
import g.sig.questweaver.domain.entities.blocks.Point
import g.sig.questweaver.domain.entities.common.Annotation
import g.sig.questweaver.domain.entities.common.TransformationData
import g.sig.questweaver.domain.entities.common.User

sealed interface GameHomeIntent {
    data object Back : GameHomeIntent

    data object Load : GameHomeIntent

    data class ChangeMode(
        val mode: AnnotationMode,
    ) : GameHomeIntent

    data class AddText(
        val anchor: Point,
    ) : GameHomeIntent

    data class AddDrawing(
        val path: List<Point>,
    ) : GameHomeIntent

    data class AddImage(
        val uri: String,
        val width: Float,
        val height: Float,
        val transformationData: TransformationData,
    ) : GameHomeIntent

    data class SelectColor(
        val color: Color,
    ) : GameHomeIntent

    data class SelectOpacity(
        val opacity: Float,
    ) : GameHomeIntent

    data class SelectSize(
        val size: Float,
    ) : GameHomeIntent

    data class SelectAnnotation(
        val annotation: Annotation,
    ) : GameHomeIntent

    data class SelectPlayer(
        val player: User,
    ) : GameHomeIntent

    data object ShowColorPicker : GameHomeIntent

    data object HideColorPicker : GameHomeIntent

    data class ChangeText(
        val id: String,
        val text: String,
    ) : GameHomeIntent

    data class CommitTransformation(
        val id: String,
        val scale: Float,
        val rotation: Float,
        val anchor: Point,
    ) : GameHomeIntent
}
