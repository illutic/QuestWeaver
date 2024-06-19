package g.sig.questweaver.hostgame.state

sealed interface HostGameIntent {
    data object Back : HostGameIntent
    data object NavigateToPermissions : HostGameIntent
    data object NavigateToQueue : HostGameIntent
    data object LoadHostGame : HostGameIntent
    data object StartHosting : HostGameIntent
    data object ShowConnectionDialog : HostGameIntent
    data class SetGameName(val gameName: String) : HostGameIntent
    data class SetDescription(val description: String) : HostGameIntent
    data class SetPlayerCount(val playerCount: Int?) : HostGameIntent
    data object CancelHostGame : HostGameIntent
}
