package g.sig.host_game.state

sealed interface HostGameIntent {
    data object Back : HostGameIntent
    data object NavigateToPermissions : HostGameIntent
    data object LoadHostGame : HostGameIntent
    data class SetGameName(val gameName: String) : HostGameIntent
    data class SetDescription(val description: String) : HostGameIntent
    data class SetPlayerCount(val playerCount: Int) : HostGameIntent
    data object StartGame : HostGameIntent
}