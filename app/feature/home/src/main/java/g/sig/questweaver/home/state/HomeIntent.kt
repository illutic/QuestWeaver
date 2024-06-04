package g.sig.questweaver.home.state

internal sealed interface HomeIntent {
    data object Back : HomeIntent
    data object FetchHome : HomeIntent
    data object NavigateToProfile : HomeIntent
    data object NavigateToSettings : HomeIntent
    data object NavigateToPermissions : HomeIntent
    data object NavigateToHost : HomeIntent
    data object NavigateToJoin : HomeIntent
    data class NavigateToGame(val gameId: String) : HomeIntent
}
