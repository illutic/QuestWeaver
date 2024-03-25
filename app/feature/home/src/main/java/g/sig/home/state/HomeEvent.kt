package g.sig.home.state

internal sealed interface HomeEvent {
    data object Idle : HomeEvent
    data object Back : HomeEvent
    data object NavigateToProfile : HomeEvent
    data object NavigateToSettings : HomeEvent
    data object NavigateToPermissions : HomeEvent
    data object NavigateToHost : HomeEvent
    data object NavigateToJoin : HomeEvent
    data class NavigateToGame(val gameId: String) : HomeEvent
}