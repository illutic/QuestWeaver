package g.sig.host_game.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import g.sig.host_game.data.HostGameViewModel
import g.sig.host_game.data.QueueViewModel
import g.sig.host_game.screens.HostGameScreen
import g.sig.host_game.screens.QueueScreen
import g.sig.host_game.state.HostGameEvent
import g.sig.host_game.state.HostGameIntent
import g.sig.host_game.state.QueueEvent
import g.sig.host_game.state.QueueIntent
import kotlinx.coroutines.flow.collectLatest

fun NavGraphBuilder.hostGameGraph(
    onBack: () -> Unit,
    onNavigateHome: () -> Unit,
    onNavigateToPermissions: () -> Unit,
    onNavigateToQueue: () -> Unit,
    onGameCreated: (id: String) -> Unit,
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
                    is HostGameEvent.Error -> event.messageId?.let { snackbarHostState.showSnackbar(context.getString(event.messageId)) }
                    HostGameEvent.CancelHostGame -> onNavigateHome()
                    HostGameEvent.NavigateToQueue -> onNavigateToQueue()
                }
            }
        }

        HostGameScreen(
            snackbarHostState = snackbarHostState,
            state = viewModel.state,
            onIntent = viewModel::handleIntent
        )
    }

    composable(HostGameRoute.QUEUE_PATH) {
        val viewModel = hiltViewModel<QueueViewModel>()
        val snackbarHostState = remember { SnackbarHostState() }

        LaunchedEffect(Unit) {
            viewModel.handleIntent(QueueIntent.Load)
            viewModel.events.collectLatest { event ->
                when (event) {
                    is QueueEvent.Error -> snackbarHostState.showSnackbar("Error")
                    is QueueEvent.GameCreated -> onGameCreated(event.id)
                    QueueEvent.CancelHostGame -> onNavigateHome()
                    QueueEvent.Back -> onBack()
                }
            }
        }

        QueueScreen(state = viewModel.state, snackbarHostState = snackbarHostState, onIntent = viewModel::handleIntent)
    }
}