package g.sig.game.state

import g.sig.navigation.Route

sealed interface GameIntent {
    data class SelectRoute(val route: Route) : GameIntent
}