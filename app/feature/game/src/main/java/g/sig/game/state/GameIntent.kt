package g.sig.game.state

import g.sig.navigation.Route

sealed interface GameIntent {
    data object Load : GameIntent
    data object RequestCloseGame : GameIntent
    data object CloseGame : GameIntent
    data class SelectRoute(val route: Route) : GameIntent
}