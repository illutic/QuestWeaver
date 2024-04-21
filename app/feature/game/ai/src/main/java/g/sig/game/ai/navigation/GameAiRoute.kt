package g.sig.game.ai.navigation

import g.sig.navigation.DecoratedRoute
import g.sig.navigation.NavIcon
import g.sig.navigation.Route

class GameAiRoute(
    override val label: String,
    override val unselectedIcon: NavIcon = NavIcon.DrawableNavIcon(g.sig.ui.R.drawable.ai_sparkles_outlined),
    override val selectedIcon: NavIcon = NavIcon.DrawableNavIcon(g.sig.ui.R.drawable.ai_sparkles_filled)
) : DecoratedRoute {
    override val route: Route = Companion

    companion object : Route {
        override val path: String = "game/ai"
    }
}