package g.sig.questweaver.hostgame.navigation

import g.sig.questweaver.navigation.Route

object HostGameRoute : Route {
    override val path: String = "host_game"
    const val QUEUE_PATH = "host_game/queue"
}
