package g.sig.game.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import g.sig.game.ai.navigation.gameAiGraph
import g.sig.game.chat.navigation.gameChatGraph
import g.sig.game.data.GameViewModel
import g.sig.game.home.navigation.GameHomeRoute
import g.sig.game.home.navigation.gameHomeGraph
import g.sig.ui.largeSize

@Composable
internal fun GameRoute(navController: NavHostController = rememberNavController()) {
    val viewModel = hiltViewModel<GameViewModel>()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            // TODO
        },
        bottomBar = {
            // TODO
        }
    ) {
        NavHost(
            modifier = Modifier
                .padding(it)
                .padding(horizontal = largeSize),
            navController = navController,
            startDestination = GameHomeRoute.path
        ) {
            gameHomeGraph()
            gameChatGraph()
            gameAiGraph()
        }
    }
}
