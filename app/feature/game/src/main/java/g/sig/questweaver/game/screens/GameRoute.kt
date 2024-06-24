package g.sig.questweaver.game.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import g.sig.questweaver.game.R
import g.sig.questweaver.game.ai.navigation.gameAiGraph
import g.sig.questweaver.game.chat.navigation.gameChatGraph
import g.sig.questweaver.game.data.GameViewModel
import g.sig.questweaver.game.home.navigation.GameHomeRoute
import g.sig.questweaver.game.home.navigation.gameHomeGraph
import g.sig.questweaver.game.state.GameIntent
import g.sig.questweaver.game.state.GameScreenEvent
import g.sig.questweaver.ui.AppIcons
import kotlinx.coroutines.flow.collectLatest

@Composable
internal fun GameRoute(
    onGameClosed: () -> Unit,
    navController: NavHostController = rememberNavController()
) {
    val viewModel = hiltViewModel<GameViewModel>()
    var showCloseGameDialog by rememberSaveable { mutableStateOf(false) }
    var showDeviceDisconnectedDialog by rememberSaveable { mutableStateOf(false) }

    BackHandler { showCloseGameDialog = true }

    LaunchedEffect(Unit) {
        viewModel.handleIntent(GameIntent.Load)
        viewModel.events.collectLatest {
            when (it) {
                is GameScreenEvent.RequestCloseGame -> showCloseGameDialog = true
                is GameScreenEvent.CloseGame -> onGameClosed()
                is GameScreenEvent.DeviceDisconnected -> showDeviceDisconnectedDialog = true
            }
        }
    }

    if (showCloseGameDialog) {
        GameAlertDialog(
            icon = AppIcons.Warning,
            title = stringResource(R.string.game_close_game_title),
            message = stringResource(R.string.game_close_game_message),
            onDismissRequest = { showCloseGameDialog = false },
            onCancel = { showCloseGameDialog = false },
            onConfirm = { viewModel.handleIntent(GameIntent.CloseGame) }
        )
    }

    if (showDeviceDisconnectedDialog) {
        GameAlertDialog(
            icon = AppIcons.Warning,
            title = stringResource(R.string.game_device_disconnected_title),
            message = stringResource(R.string.game_device_disconnected_message),
            onDismissRequest = { onGameClosed() },
            onConfirm = { onGameClosed() }
        )
    }

    GameNavigation(
        modifier = Modifier.fillMaxSize(),
        selectedRoute = viewModel.selectedRoute,
        routes = viewModel.gameRoutes,
        onItemClick = { viewModel.handleIntent(GameIntent.SelectRoute(it)) }
    ) {
        NavHost(
            navController = navController,
            startDestination = GameHomeRoute.path
        ) {
            gameHomeGraph { showCloseGameDialog = true }
            gameChatGraph()
            gameAiGraph()
        }
    }
}

@Composable
private fun GameAlertDialog(
    icon: Painter,
    title: String,
    message: String = "",
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    onCancel: (() -> Unit)? = null
) {
    AlertDialog(
        icon = {
            Icon(
                painter = icon,
                contentDescription = null
            )
        },
        onDismissRequest = onDismissRequest,
        title = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.titleLarge,
                text = title
            )
        },
        text = {
            Text(text = message)
        },
        dismissButton = {
            if (onCancel != null) {
                TextButton(onClick = { onDismissRequest() }) {
                    Text(text = stringResource(R.string.cancel))
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onConfirm()
                onDismissRequest()
            }) {
                Text(text = stringResource(R.string.confirm))
            }
        }
    )
}
