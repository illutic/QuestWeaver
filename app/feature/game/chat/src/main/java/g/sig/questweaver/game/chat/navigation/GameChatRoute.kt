package g.sig.questweaver.game.chat.navigation

import g.sig.questweaver.navigation.DecoratedRoute
import g.sig.questweaver.navigation.NavIcon
import g.sig.questweaver.navigation.Route

class GameChatRoute(
    override val label: String,
    override val unselectedIcon: NavIcon = NavIcon.DrawableNavIcon(g.sig.questweaver.ui.R.drawable.chat_outlined),
    override val selectedIcon: NavIcon = NavIcon.DrawableNavIcon(g.sig.questweaver.ui.R.drawable.chat_filled),
) : DecoratedRoute {
    override val path: String = Companion.path

    companion object : Route {
        override val path: String = "game/chat"
    }
}
