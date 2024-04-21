package g.sig.game.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import g.sig.game.home.screens.GameHomeRoute

fun NavGraphBuilder.gameHomeGraph() {
    composable(GameHomeRoute.path) {
        GameHomeRoute()
    }
}