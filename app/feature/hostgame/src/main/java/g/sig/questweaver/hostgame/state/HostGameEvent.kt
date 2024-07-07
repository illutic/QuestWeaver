package g.sig.questweaver.hostgame.state

import androidx.annotation.StringRes

sealed interface HostGameEvent {
    data object Back : HostGameEvent

    data object NavigateToPermissions : HostGameEvent

    data object NavigateToQueue : HostGameEvent

    data object CancelHostGame : HostGameEvent

    data class Error(
        @StringRes val messageId: Int? = null,
    ) : HostGameEvent
}
