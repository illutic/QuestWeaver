package g.sig.host_game.state

import androidx.annotation.StringRes

sealed interface QueueEvent {
    data object Back : QueueEvent
    data class GameCreated(val id: String) : QueueEvent
    data object CancelHostGame : QueueEvent
    data class Error(@StringRes val message: Int) : QueueEvent
}