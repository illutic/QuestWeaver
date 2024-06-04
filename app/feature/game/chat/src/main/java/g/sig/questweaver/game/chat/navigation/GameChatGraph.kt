package g.sig.questweaver.game.chat.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import g.sig.questweaver.game.chat.screens.GameChatRoute

fun NavGraphBuilder.gameChatGraph() {
    composable(GameChatRoute.path) {
        GameChatRoute()
    }
}
