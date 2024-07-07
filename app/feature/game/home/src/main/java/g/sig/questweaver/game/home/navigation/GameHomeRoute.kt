package g.sig.questweaver.game.home.navigation

import g.sig.questweaver.navigation.DecoratedRoute
import g.sig.questweaver.navigation.NavIcon
import g.sig.questweaver.navigation.Route
import g.sig.questweaver.ui.AppIcons

class GameHomeRoute(
    override val label: String,
    override val unselectedIcon: NavIcon = NavIcon.VectorNavIcon(AppIcons.HomeOutlinedVector),
    override val selectedIcon: NavIcon = NavIcon.VectorNavIcon(AppIcons.HomeFilledVector),
) : DecoratedRoute {
    override val path: String = Companion.path

    companion object : Route {
        override val path: String = "game/home"
    }
}
