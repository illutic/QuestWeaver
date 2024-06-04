package g.sig.questweaver.game.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import g.sig.questweaver.game.screens.GameRoute

fun NavGraphBuilder.gameGraph(onGameClosed: () -> Unit) {
    composable(
        route = GameRoute.path,
        arguments = GameRoute.arguments
    ) {
        GameRoute(onGameClosed)
    }
}
