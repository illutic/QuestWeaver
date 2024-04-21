package g.sig.game.chat.navigation

import g.sig.navigation.DecoratedRoute
import g.sig.navigation.NavIcon
import g.sig.navigation.Route

class GameChatRoute(
    override val label: String,
    override val unselectedIcon: NavIcon = NavIcon.DrawableNavIcon(g.sig.ui.R.drawable.chat_outlined),
    override val selectedIcon: NavIcon = NavIcon.DrawableNavIcon(g.sig.ui.R.drawable.chat_filled)
) : DecoratedRoute {
    override val route: Route = Companion

    companion object : Route {
        override val path: String = "game/chat"
    }
}