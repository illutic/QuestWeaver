package g.sig.game.chat.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import g.sig.game.chat.screens.GameChatRoute

fun NavGraphBuilder.gameChatGraph() {
    composable(GameChatRoute.path) {
        GameChatRoute()
    }
}