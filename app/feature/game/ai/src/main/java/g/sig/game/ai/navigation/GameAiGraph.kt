package g.sig.game.ai.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import g.sig.game.ai.screens.GameAiRoute

fun NavGraphBuilder.gameAiGraph() {
    composable(GameAiRoute.path) {
        GameAiRoute()
    }
}