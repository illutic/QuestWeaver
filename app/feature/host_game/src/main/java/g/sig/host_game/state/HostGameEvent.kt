package g.sig.host_game.state

import androidx.annotation.StringRes

sealed interface HostGameEvent {
    data object Back : HostGameEvent
    data object NavigateToPermissions : HostGameEvent
    data object GameCreated : HostGameEvent
    data class Error(@StringRes val messageId: Int? = null) : HostGameEvent
}