package g.sig.host_game.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import g.sig.host_game.data.HostGameViewModel
import g.sig.host_game.screens.HostGameScreen
import g.sig.host_game.state.HostGameEvent
import g.sig.host_game.state.HostGameIntent
import kotlinx.coroutines.flow.collectLatest

fun NavGraphBuilder.hostGameGraph(
    onBack: () -> Unit,
    onNavigateToPermissions: () -> Unit,
    onGameCreated: () -> Unit,
) {
    composable(HostGameRoute.path) {
        val viewModel = hiltViewModel<HostGameViewModel>()
        val snackbarHostState = remember { SnackbarHostState() }
        val context = LocalContext.current

        LaunchedEffect(Unit) {
            viewModel.handleIntent(HostGameIntent.LoadHostGame)
            viewModel.events.collectLatest { event ->
                when (event) {
                    HostGameEvent.Back -> onBack()
                    HostGameEvent.NavigateToPermissions -> onNavigateToPermissions()
                    HostGameEvent.GameCreated -> onGameCreated()
                    is HostGameEvent.Error -> event.messageId?.let { snackbarHostState.showSnackbar(context.getString(event.messageId)) }
                }
            }
        }

        HostGameScreen(
            snackbarHostState = snackbarHostState,
            state = viewModel.state,
            onIntent = viewModel::handleIntent
        )
    }
}