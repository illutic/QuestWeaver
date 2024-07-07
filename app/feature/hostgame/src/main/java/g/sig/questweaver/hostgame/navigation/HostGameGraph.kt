package g.sig.questweaver.hostgame.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import g.sig.questweaver.hostgame.data.HostGameViewModel
import g.sig.questweaver.hostgame.data.QueueViewModel
import g.sig.questweaver.hostgame.screens.HostGameScreen
import g.sig.questweaver.hostgame.screens.QueueScreen
import g.sig.questweaver.hostgame.state.HostGameEvent
import g.sig.questweaver.hostgame.state.HostGameIntent
import g.sig.questweaver.hostgame.state.QueueEvent
import g.sig.questweaver.hostgame.state.QueueIntent
import g.sig.questweaver.navigation.SharedElementKeys
import g.sig.questweaver.ui.sharedBounds
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
                    is HostGameEvent.Error ->
                        event.messageId?.let {
                            snackbarHostState.showSnackbar(
                                context.getString(event.messageId),
                            )
                        }

                    HostGameEvent.CancelHostGame -> onNavigateHome()
                    HostGameEvent.NavigateToQueue -> onNavigateToQueue()
                }
            }
        }

        HostGameScreen(
            modifier = Modifier.sharedBounds(SharedElementKeys.HOST_KEY, this),
            snackbarHostState = snackbarHostState,
            state = viewModel.state,
            animationScope = this,
            onIntent = viewModel::handleIntent,
        )
    }

    composable(HostGameRoute.QUEUE_PATH, HostGameRoute.queueArguments) {
        val viewModel = hiltViewModel<QueueViewModel>()
        val snackbarHostState = remember { SnackbarHostState() }
        val gameIdArgument = it.arguments?.getString("id")

        LaunchedEffect(Unit) {
            viewModel.handleIntent(QueueIntent.Load(gameIdArgument))
            viewModel.events.collectLatest { event ->
                when (event) {
                    is QueueEvent.Error -> snackbarHostState.showSnackbar("Error")
                    is QueueEvent.GameCreated -> onGameCreated(event.id)
                    QueueEvent.CancelHostGame -> onNavigateHome()
                    QueueEvent.Back -> onBack()
                }
            }
        }

        QueueScreen(
            modifier = Modifier.sharedBounds(SharedElementKeys.HOST_QUEUE_KEY, this),
            state = viewModel.state,
            snackbarHostState = snackbarHostState,
            onIntent = viewModel::handleIntent,
        )
    }
}
