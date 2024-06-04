package g.sig.questweaver.game.state

import g.sig.questweaver.navigation.Route

sealed interface GameIntent {
    data object Load : GameIntent
    data object RequestCloseGame : GameIntent
    data object CloseGame : GameIntent
    data class SelectRoute(val route: Route) : GameIntent
}
