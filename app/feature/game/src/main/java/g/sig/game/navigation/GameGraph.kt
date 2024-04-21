package g.sig.game.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import g.sig.game.screens.GameRoute

fun NavGraphBuilder.gameGraph() {
    composable(route = GameRoute.path) {
        GameRoute()
    }
}
