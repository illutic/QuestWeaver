package g.sig.game.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
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
import g.sig.game.state.GameIntent
import g.sig.ui.largeSize

@Composable
internal fun GameRoute(navController: NavHostController = rememberNavController()) {
    val viewModel = hiltViewModel<GameViewModel>()

    GameNavigation(
        modifier = Modifier.fillMaxSize(),
        selectedRoute = viewModel.selectedRoute,
        routes = viewModel.gameRoutes,
        onItemClick = { viewModel.handleIntent(GameIntent.SelectRoute(it)) }
    ) {
        NavHost(
            modifier = Modifier
                .safeContentPadding()
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