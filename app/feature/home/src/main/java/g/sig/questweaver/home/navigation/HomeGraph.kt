package g.sig.questweaver.home.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import g.sig.questweaver.domain.entities.common.Game
import g.sig.questweaver.home.data.HomeViewModel
import g.sig.questweaver.home.screens.GameNotFoundDialog
import g.sig.questweaver.home.screens.HomeScreen
import g.sig.questweaver.home.state.HomeEvent
import g.sig.questweaver.home.state.HomeIntent
import kotlinx.coroutines.flow.collectLatest

fun NavGraphBuilder.homeGraph(
    onNavigateToOnboarding: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToHostGame: () -> Unit,
    onNavigateToJoinGame: () -> Unit,
    onNavigateToPermissions: () -> Unit,
    onNavigateToQueue: (String) -> Unit,
    onNavigateToGame: (String) -> Unit,
) {
    composable(HomeRoute.path) {
        val viewModel = hiltViewModel<HomeViewModel>()
        val state by viewModel.state.collectAsState()
        var gameNotFound by remember { mutableStateOf<Game?>(null) }

        LaunchedEffect(Unit) {
            viewModel.handleIntent(HomeIntent.FetchHome)
            viewModel.events.collectLatest { event ->
                when (event) {
                    HomeEvent.NavigateToOnboarding -> onNavigateToOnboarding()
                    HomeEvent.NavigateToProfile -> onNavigateToProfile()
                    HomeEvent.NavigateToSettings -> onNavigateToSettings()
                    HomeEvent.NavigateToHost -> onNavigateToHostGame()
                    HomeEvent.NavigateToJoin -> onNavigateToJoinGame()
                    HomeEvent.NavigateToPermissions -> onNavigateToPermissions()

                    is HomeEvent.NavigateToGame -> onNavigateToGame(event.gameId)
                    is HomeEvent.NavigateToQueue -> onNavigateToQueue(event.gameId)
                    is HomeEvent.GameNotFound -> gameNotFound = event.game

                    else -> {}
                }
            }
        }

        gameNotFound?.let {
            GameNotFoundDialog(
                game = it,
                onDismiss = { gameNotFound = null },
                onHostGame = onNavigateToQueue,
                onRemoveGame = { gameId ->
                    viewModel.handleIntent(HomeIntent.RemoveGame(gameId))
                },
            )
        }

        HomeScreen(
            homeState = state,
            animationScope = this,
            onIntent = viewModel::handleIntent,
        )
    }
}
