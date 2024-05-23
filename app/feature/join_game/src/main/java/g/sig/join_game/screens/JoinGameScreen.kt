package g.sig.join_game.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import g.sig.common.ui.PermissionsAlert
import g.sig.domain.entities.ConnectionState
import g.sig.domain.entities.Device
import g.sig.join_game.R
import g.sig.join_game.state.JoinGameIntent
import g.sig.join_game.state.JoinGameState
import g.sig.ui.AppIcons
import g.sig.ui.largeSize
import g.sig.ui.mediumSize

@Composable
internal fun JoinGameScreen(state: JoinGameState, onIntent: (JoinGameIntent) -> Unit) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { JoinGameTopBar { onIntent(JoinGameIntent.Back) } },
    ) { padding ->
        var showDeviceConfirmationDialog by remember { mutableStateOf<JoinGameState.ShowDeviceConfirmationDialog?>(null) }
        showDeviceConfirmationDialog?.let {
            ConnectionDialog(
                deviceName = it.device.name,
                onDismissRequest = { showDeviceConfirmationDialog = null },
                onConfirm = { onIntent(JoinGameIntent.RequestConnection(it.device)) }
            )
        }

        JoinGameScreenContent(
            modifier = Modifier.padding(padding),
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
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = largeSize),
        verticalArrangement = spacedBy(largeSize),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            AsyncImage(
                modifier = Modifier.width(360.dp),
                model = R.drawable.graphic_8,
                contentDescription = ""
            )
        }

        if (state.discovering && state.hasPermissions) {
            item {
                LinearProgressIndicator(Modifier.width(IntrinsicSize.Max))
            }
        } else {
            item {
                Button(onClick = { onIntent(JoinGameIntent.Load) }) {
                    Text(text = stringResource(R.string.join_game_refresh))
                }
            }
        }

        if (!state.hasPermissions) {
            item {
                PermissionsAlert(onClick = { onIntent(JoinGameIntent.NavigateToPermissions) })
            }
        }

        if (state.devices.isNotEmpty()) {
            items(state.devices) { deviceState ->
                DeviceCard(device = deviceState, onDeviceClicked = onDeviceClicked)
            }
        } else if (state.hasPermissions) {
            item {
                Text(
                    text = stringResource(R.string.join_game_empty),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
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

@Composable
private fun DeviceCard(
    modifier: Modifier = Modifier,
    device: Device,
    onDeviceClicked: (Device) -> Unit
) {
    fun <T> defaultAnimation() = tween<T>(1000)

    val contentColors by animateColorAsState(
        targetValue = when (device.connectionState) {
            is ConnectionState.Found,
            ConnectionState.Idle -> contentColorFor(MaterialTheme.colorScheme.surface)

            is ConnectionState.Connected -> contentColorFor(MaterialTheme.colorScheme.primaryContainer)

            ConnectionState.Loading,
            is ConnectionState.Connecting -> contentColorFor(MaterialTheme.colorScheme.secondaryContainer)

            is ConnectionState.Error -> contentColorFor(MaterialTheme.colorScheme.errorContainer)
        },
        animationSpec = defaultAnimation(),
        label = "content color"
    )

    val backgroundColor by animateColorAsState(
        targetValue = when (device.connectionState) {
            is ConnectionState.Found,
            ConnectionState.Idle -> MaterialTheme.colorScheme.surface

            is ConnectionState.Connected -> MaterialTheme.colorScheme.primaryContainer

            ConnectionState.Loading,
            is ConnectionState.Connecting -> MaterialTheme.colorScheme.secondaryContainer

            is ConnectionState.Error -> MaterialTheme.colorScheme.errorContainer
        },
        animationSpec = defaultAnimation(),
        label = "background color"
    )

    Surface(
        modifier = modifier,
        tonalElevation = mediumSize,
        color = backgroundColor,
        contentColor = contentColors,
        border = BorderStroke(JoinGameSize.borderWidth, contentColors.copy(alpha = 0.5f)),
        shape = MaterialTheme.shapes.medium,
        enabled = device.connectionState == ConnectionState.Idle,
        onClick = { onDeviceClicked(device) }
    ) {
        Row(
            modifier = Modifier.padding(largeSize),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = device.name,
                style = MaterialTheme.typography.titleSmall
            )

            AnimatedContent(
                targetState = device.connectionState,
                label = "icon",
                transitionSpec = {
                    fadeIn(animationSpec = defaultAnimation()) + scaleIn(animationSpec = defaultAnimation()) togetherWith
                            fadeOut(animationSpec = defaultAnimation()) + scaleOut(animationSpec = defaultAnimation())
                }
            ) { state ->
                when (state) {
                    is ConnectionState.Found,
                    ConnectionState.Idle -> {
                        Icon(
                            modifier = Modifier
                                .size(JoinGameSize.iconSize)
                                .padding(start = mediumSize),
                            painter = AppIcons.ChevronRight,
                            contentDescription = null
                        )
                    }

                    is ConnectionState.Connected -> {
                        Icon(
                            modifier = Modifier
                                .size(JoinGameSize.iconSize)
                                .padding(start = mediumSize),
                            painter = AppIcons.Check,
                            contentDescription = null
                        )
                    }

                    ConnectionState.Loading,
                    is ConnectionState.Connecting -> {
                        CircularProgressIndicator(
                            modifier = Modifier.size(JoinGameSize.iconSize),
                            color = contentColors,
                            trackColor = contentColors.copy(alpha = 0.2f),
                            strokeCap = StrokeCap.Round,
                        )
                    }

                    is ConnectionState.Error -> {
                        Icon(
                            modifier = Modifier
                                .size(JoinGameSize.iconSize)
                                .padding(start = mediumSize),
                            painter = AppIcons.Close,
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun JoinGameTopBar(onBack: () -> Unit) {
    TopAppBar(modifier = Modifier.fillMaxWidth(),
        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
        title = {
            Text(text = stringResource(R.string.join_game_title))
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(AppIcons.Back, contentDescription = null)
            }
        })
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
                Device("3", "Device 3", ConnectionState.Error.ConnectionRequestError),
            )
        }
    ) {}
}