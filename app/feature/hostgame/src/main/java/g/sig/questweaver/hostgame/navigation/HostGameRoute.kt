package g.sig.questweaver.hostgame.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import g.sig.questweaver.navigation.Route

object HostGameRoute : Route {
    override val path: String = "host_game"

    const val QUEUE_PATH: String = "host_game/queue/{id}"
    val queueArguments: List<NamedNavArgument> =
        listOf(
            navArgument("id") { type = NavType.StringType },
        )

    fun createPath(id: String): String = "host_game/queue/$id"
}
