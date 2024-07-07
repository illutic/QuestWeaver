package g.sig.questweaver.hostgame.state

import androidx.annotation.StringRes

sealed interface QueueEvent {
    data object Back : QueueEvent

    data class GameCreated(
        val id: String,
    ) : QueueEvent

    data object CancelHostGame : QueueEvent

    data class Error(
        @StringRes val message: Int,
    ) : QueueEvent
}
