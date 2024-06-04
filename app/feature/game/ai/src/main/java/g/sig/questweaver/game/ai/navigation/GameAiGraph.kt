package g.sig.questweaver.game.ai.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import g.sig.questweaver.game.ai.screens.GameAiRoute

fun NavGraphBuilder.gameAiGraph() {
    composable(GameAiRoute.path) {
        GameAiRoute()
    }
}
