package g.sig.questweaver.joingame.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import g.sig.questweaver.joingame.data.JoinGameViewModel
import g.sig.questweaver.joingame.screens.JoinGameScreen
import g.sig.questweaver.joingame.state.JoinGameEvent
import g.sig.questweaver.joingame.state.JoinGameIntent
import kotlinx.coroutines.flow.collectLatest

fun NavGraphBuilder.joinGameGraph(
    onBack: () -> Unit,
    onNavigateToPermissions: () -> Unit,
    onNavigateToGame: (String) -> Unit
) {
    composable(JoinGameRoute.path) {
        val viewModel = hiltViewModel<JoinGameViewModel>()

        LaunchedEffect(Unit) {
            viewModel.handleIntent(JoinGameIntent.Load)
            viewModel.events.collectLatest { event ->
                when (event) {
                    JoinGameEvent.Back -> onBack()
                    JoinGameEvent.NavigateToPermissions -> onNavigateToPermissions()
                    is JoinGameEvent.JoinGame -> onNavigateToGame(event.game.gameId)
                }
            }
        }

        JoinGameScreen(state = viewModel.state, onIntent = viewModel::handleIntent)
    }
}
