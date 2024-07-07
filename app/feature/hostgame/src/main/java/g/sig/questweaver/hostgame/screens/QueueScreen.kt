package g.sig.questweaver.hostgame.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import g.sig.questweaver.common.ui.components.DeviceCard
import g.sig.questweaver.common.ui.components.ImageWithPlaceholder
import g.sig.questweaver.common.ui.layouts.ScreenScaffold
import g.sig.questweaver.domain.entities.common.Device
import g.sig.questweaver.domain.entities.states.ConnectionState
import g.sig.questweaver.hostgame.R
import g.sig.questweaver.hostgame.state.QueueIntent
import g.sig.questweaver.hostgame.state.QueueState
import g.sig.questweaver.ui.AppIcons
import g.sig.questweaver.ui.largeSize
import g.sig.questweaver.ui.mediumSize

@Composable
internal fun QueueScreen(
    state: QueueState,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    onIntent: (QueueIntent) -> Unit,
) {
    ScreenScaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = { QueueScreenTopBar { onIntent(QueueIntent.Back) } },
        navigation = {
            Button(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = largeSize),
                onClick = { onIntent(QueueIntent.StartGame) },
            ) {
                Text(text = stringResource(R.string.queue_game_button))
            }
        },
    ) {
        JoinGameScreenContent(
            state = state,
            onIntent = onIntent,
        )
    }
}

@Composable
private fun JoinGameScreenContent(
    modifier: Modifier = Modifier,
    state: QueueState,
    onIntent: (QueueIntent) -> Unit,
) {
    val scrollState = rememberScrollState()

    ImageWithPlaceholder(
        modifier =
            Modifier
                .verticalScroll(scrollState)
                .width(HostGameSize.imageSize),
        model = R.drawable.graphic_10,
        contentDescription = "",
    )

    Column(
        modifier =
            modifier
                .verticalScroll(scrollState)
                .padding(largeSize),
        verticalArrangement = Arrangement.spacedBy(largeSize),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (state.advertising) {
            LinearProgressIndicator(Modifier.width(IntrinsicSize.Max))
        }

        if (state.devicesToConnect.isNotEmpty()) {
            state.devicesToConnect.forEach { deviceState ->
                DeviceCard(
                    modifier = Modifier.widthIn(max = HostGameSize.maxDeviceCardSize),
                    device = deviceState,
                    trailingContent = {
                        DeviceCardAcceptAndDenyControls(
                            onAccepted = { onIntent(QueueIntent.AcceptConnection(deviceState)) },
                            onDenied = { onIntent(QueueIntent.RejectConnection(deviceState)) },
                        )
                    },
                )
            }
        } else {
            Text(
                text = stringResource(R.string.queue_empty),
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Composable
private fun DeviceCardAcceptAndDenyControls(
    onAccepted: () -> Unit,
    onDenied: () -> Unit,
) {
    Row(horizontalArrangement = Arrangement.spacedBy(mediumSize)) {
        IconButton(
            onClick = { onAccepted() },
            modifier = Modifier.size(HostGameSize.iconSize),
        ) {
            Icon(
                tint = MaterialTheme.colorScheme.primary,
                painter = AppIcons.Check,
                contentDescription = null,
            )
        }

        IconButton(
            onClick = { onDenied() },
            modifier = Modifier.size(HostGameSize.iconSize),
        ) {
            Icon(
                tint = MaterialTheme.colorScheme.error,
                painter = AppIcons.Close,
                contentDescription = null,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun QueueScreenTopBar(onBack: () -> Unit) {
    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
        title = {
            Text(text = stringResource(R.string.queue_title))
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(AppIcons.Back, contentDescription = null)
            }
        },
    )
}

@Preview
@Composable
fun PreviewQueueScreen() {
    QueueScreen(
        state =
            QueueState().apply {
                devicesToConnect.addAll(
                    listOf(
                        Device("1", "Device 1", ConnectionState.Idle),
                        Device("2", "Device 2", ConnectionState.Idle),
                        Device("3", "Device 3", ConnectionState.Idle),
                    ),
                )
                advertising = false
            },
        snackbarHostState = SnackbarHostState(),
        onIntent = {},
    )
}
