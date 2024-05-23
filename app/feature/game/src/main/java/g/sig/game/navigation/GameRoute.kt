package g.sig.game.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import g.sig.navigation.Route

object GameRoute : Route {
    override val path: String = "game/{id}"
    override val arguments: List<NamedNavArgument> = listOf(
        navArgument("id") { type = NavType.StringType }
    )

    fun createPath(id: String): String = "game/$id"
}