package g.sig.game.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import g.sig.game.home.screens.GameHomeRoute

fun NavGraphBuilder.gameHomeGraph(onBack: () -> Unit) {
    composable(GameHomeRoute.path) {
        GameHomeRoute(onBack)
    }
}