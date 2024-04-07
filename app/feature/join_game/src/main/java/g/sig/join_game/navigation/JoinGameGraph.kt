package g.sig.join_game.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import g.sig.join_game.data.JoinGameViewModel
import g.sig.join_game.screens.JoinGameScreen
import g.sig.join_game.state.JoinGameEvent
import g.sig.join_game.state.JoinGameIntent
import kotlinx.coroutines.flow.collectLatest

fun NavGraphBuilder.joinGameGraph(onBack: () -> Unit, onNavigateToPermissions: () -> Unit, onNavigateToGame: (String) -> Unit) {
    composable(JoinGameRoute.path) {
        val viewModel = hiltViewModel<JoinGameViewModel>()

        LaunchedEffect(Unit) {
            viewModel.handleIntent(JoinGameIntent.LoadGames)
            viewModel.events.collectLatest { event ->
                when (event) {
                    JoinGameEvent.Back -> onBack()
                    JoinGameEvent.NavigateToPermissions -> onNavigateToPermissions()
                    is JoinGameEvent.JoinGame -> onNavigateToGame(event.game.id)
                }
            }
        }

        JoinGameScreen(state = viewModel.state, onIntent = viewModel::handleIntent)
    }
}