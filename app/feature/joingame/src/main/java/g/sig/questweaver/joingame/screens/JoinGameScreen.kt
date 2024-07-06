package g.sig.questweaver.joingame.screens

import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import g.sig.questweaver.common.ui.components.AdaptiveProgressBar
import g.sig.questweaver.common.ui.components.DeviceCard
import g.sig.questweaver.common.ui.components.ImageWithPlaceholder
import g.sig.questweaver.common.ui.components.PermissionsAlert
import g.sig.questweaver.common.ui.layouts.ScreenScaffold
import g.sig.questweaver.domain.entities.common.Device
import g.sig.questweaver.domain.entities.states.ConnectionState
import g.sig.questweaver.joingame.R
import g.sig.questweaver.joingame.state.JoinGameIntent
import g.sig.questweaver.joingame.state.JoinGameState
import g.sig.questweaver.ui.AppIcons
import g.sig.questweaver.ui.largeSize

@Composable
internal fun JoinGameScreen(
    state: JoinGameState,
    modifier: Modifier = Modifier,
    onIntent: (JoinGameIntent) -> Unit
) {
    ScreenScaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { JoinGameTopBar { onIntent(JoinGameIntent.Back) } },
    ) {
        var showDeviceConfirmationDialog by remember {
            mutableStateOf<JoinGameState.ShowDeviceConfirmationDialog?>(
                null
            )
        }
        showDeviceConfirmationDialog?.let {
            ConnectionDialog(
                deviceName = it.device.name,
                onDismissRequest = { showDeviceConfirmationDialog = null },
                onConfirm = { onIntent(JoinGameIntent.RequestConnection(it.device)) }
            )
        }

        JoinGameScreenContent(
            modifier = modifier,
            state = state,
            onDeviceClicked = { device ->
                showDeviceConfirmationDialog = JoinGameState.ShowDeviceConfirmationDialog(device)
            },
            onIntent = onIntent
        )
    }
}

@Composable
private fun JoinGameScreenContent(
    modifier: Modifier = Modifier,
    state: JoinGameState,
    onDeviceClicked: (Device) -> Unit,
    onIntent: (JoinGameIntent) -> Unit
) {
    val scrollState = rememberScrollState()

    ImageWithPlaceholder(
        modifier = Modifier
            .verticalScroll(scrollState)
            .size(JoinGameSize.imageSize),
        model = R.drawable.graphic_8,
        contentDescription = ""
    )

    Column(
        modifier = modifier
            .verticalScroll(scrollState)
            .padding(horizontal = largeSize),
        verticalArrangement = spacedBy(largeSize),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (state.discovering && state.hasPermissions) {
            AdaptiveProgressBar(Modifier.width(IntrinsicSize.Max))
        } else {
            Button(onClick = { onIntent(JoinGameIntent.Load) }) {
                Text(text = stringResource(R.string.join_game_refresh))
            }
        }

        if (!state.hasPermissions) {
            PermissionsAlert(onClick = { onIntent(JoinGameIntent.NavigateToPermissions) })
        }

        if (state.devices.isNotEmpty()) {
            state.devices.forEach { deviceState ->
                DeviceCard(device = deviceState, onDeviceClicked = onDeviceClicked)
            }
        } else if (state.hasPermissions) {
            Text(
                text = stringResource(R.string.join_game_empty),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
internal fun ConnectionDialog(
    deviceName: String,
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        icon = {
            Icon(
                painter = AppIcons.ConnectingDevice,
                contentDescription = null
            )
        },
        onDismissRequest = onDismissRequest,
        title = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.titleLarge,
                text = stringResource(R.string.join_game_dialog_title, deviceName)
            )
        },
        text = {
            Text(text = stringResource(R.string.join_game_dialog_message))
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = stringResource(R.string.join_game_dialog_no))
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onConfirm()
                onDismissRequest()
            }) {
                Text(text = stringResource(R.string.join_game_dialog_yes))
            }
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun JoinGameTopBar(onBack: () -> Unit) {
    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
        title = {
            Text(text = stringResource(R.string.join_game_title))
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(AppIcons.Back, contentDescription = null)
            }
        }
    )
}

@Preview
@Composable
private fun JoinGameScreenPreview() {
    JoinGameScreen(
        state = JoinGameState().apply {
            devices = mutableStateListOf(
                Device("1", "Device 1", ConnectionState.Idle),
                Device("2", "Device 2", ConnectionState.Connecting("2", "Device 2")),
                Device("3", "Device 3", ConnectionState.Connected("2")),
                Device("3", "Device 3", ConnectionState.Error.GenericError("123", null)),
            )
        }
    ) {}
}
