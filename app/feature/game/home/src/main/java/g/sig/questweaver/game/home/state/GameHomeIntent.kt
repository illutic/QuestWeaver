package g.sig.questweaver.game.home.state

import g.sig.questweaver.domain.entities.blocks.Point
import kotlinx.coroutines.flow.Flow

sealed interface GameHomeIntent {
    data object Back : GameHomeIntent
    sealed interface Request : GameHomeIntent {
        data object RequestTextEdit : Request
        data object RequestDrawing : Request
        data object RequestSizeChange : Request
        data object RequestOpacityChange : Request
        data object RequestColorChange : Request
        data object RequestDMTools : Request
    }

    sealed interface Select : GameHomeIntent {
        data class SelectText(val text: String) : Select
        data class SelectColor(val color: String) : Select
        data class SelectSize(val size: Float) : Select
        data class SelectOpacity(val opacity: Float) : Select
    }

    data class Drawing(val points: Flow<Point>)
}
