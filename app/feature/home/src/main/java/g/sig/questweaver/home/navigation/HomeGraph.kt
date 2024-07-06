package g.sig.questweaver.home.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import g.sig.questweaver.home.data.HomeViewModel
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
    onNavigateToGame: (String) -> Unit,
) {
    composable(HomeRoute.path) {
        val viewModel = hiltViewModel<HomeViewModel>()
        val state by viewModel.state.collectAsState()

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
                    else -> {}
                }
            }
        }

        HomeScreen(
            homeState = state,
            animationScope = this,
            onIntent = viewModel::handleIntent
        )
    }
}
