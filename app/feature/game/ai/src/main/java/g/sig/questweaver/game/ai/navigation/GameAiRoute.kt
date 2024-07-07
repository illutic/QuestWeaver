package g.sig.questweaver.game.ai.navigation

import g.sig.questweaver.navigation.DecoratedRoute
import g.sig.questweaver.navigation.NavIcon
import g.sig.questweaver.navigation.Route

class GameAiRoute(
    override val label: String,
    override val unselectedIcon: NavIcon =
        NavIcon.DrawableNavIcon(g.sig.questweaver.ui.R.drawable.ai_sparkles_outlined),
    override val selectedIcon: NavIcon =
        NavIcon.DrawableNavIcon(g.sig.questweaver.ui.R.drawable.ai_sparkles_filled),
) : DecoratedRoute {
    override val path: String = Companion.path

    companion object : Route {
        override val path: String = "game/ai"
    }
}
