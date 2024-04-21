package g.sig.game.home.navigation

import g.sig.navigation.DecoratedRoute
import g.sig.navigation.NavIcon
import g.sig.navigation.Route
import g.sig.ui.AppIcons

class GameHomeRoute(
    override val label: String,
    override val unselectedIcon: NavIcon = NavIcon.VectorNavIcon(AppIcons.HomeOutlinedVector),
    override val selectedIcon: NavIcon = NavIcon.VectorNavIcon(AppIcons.HomeFilledVector)
) : DecoratedRoute {
    override val route: Route = Companion

    companion object : Route {
        override val path: String = "game/home"
    }
}