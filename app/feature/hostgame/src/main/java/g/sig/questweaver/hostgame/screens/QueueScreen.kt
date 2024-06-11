package g.sig.questweaver.hostgame.screens

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
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import g.sig.questweaver.common.ui.components.AdaptiveImage
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
    onIntent: (QueueIntent) -> Unit
) {
    ScreenScaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = { QueueScreenTopBar { onIntent(QueueIntent.Back) } },
        decoration = {
            AdaptiveImage(
                modifier = Modifier.width(HostGameSize.imageSize),
                model = R.drawable.graphic_10,
                contentDescription = ""
            )
        },
        navigation = {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = largeSize),
                onClick = { onIntent(QueueIntent.StartGame) }
            ) {
                Text(text = stringResource(R.string.queue_game_button))
            }
        }
    ) {
        JoinGameScreenContent(
            state = state,
            onIntent = onIntent
        )
    }
}

@Composable
private fun JoinGameScreenContent(
    modifier: Modifier = Modifier,
    state: QueueState,
    onIntent: (QueueIntent) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = largeSize),
        verticalArrangement = Arrangement.spacedBy(largeSize),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (state.advertising) {
            item {
                LinearProgressIndicator(Modifier.width(IntrinsicSize.Max))
            }
        }

        if (state.devicesToConnect.isNotEmpty()) {
            items(state.devicesToConnect) { deviceState ->
                DeviceCard(
                    device = deviceState,
                    onAcceptClicked = { onIntent(QueueIntent.AcceptConnection(it)) },
                    onRejectClicked = { onIntent(QueueIntent.RejectConnection(it)) }
                )
            }
        } else {
            item {
                Text(
                    text = stringResource(R.string.queue_empty),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
private fun DeviceCard(
    modifier: Modifier = Modifier,
    device: Device,
    onAcceptClicked: (Device) -> Unit,
    onRejectClicked: (Device) -> Unit
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
        border = BorderStroke(HostGameSize.borderWidth, contentColors.copy(alpha = 0.5f)),
        shape = MaterialTheme.shapes.medium
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
                    val enterTransition = fadeIn(defaultAnimation()) + scaleIn(defaultAnimation())
                    val exitTransition = fadeOut(defaultAnimation()) + scaleOut(defaultAnimation())
                    enterTransition togetherWith exitTransition
                }
            ) { state ->
                when (state) {
                    is ConnectionState.Found,
                    ConnectionState.Idle -> {
                        Row(horizontalArrangement = Arrangement.spacedBy(mediumSize)) {
                            IconButton(
                                onClick = { onAcceptClicked(device) },
                                modifier = Modifier.size(HostGameSize.iconSize)
                            ) {
                                Icon(
                                    tint = MaterialTheme.colorScheme.primary,
                                    painter = AppIcons.Check,
                                    contentDescription = null
                                )
                            }

                            IconButton(
                                onClick = { onRejectClicked(device) },
                                modifier = Modifier.size(HostGameSize.iconSize)
                            ) {
                                Icon(
                                    tint = MaterialTheme.colorScheme.error,
                                    painter = AppIcons.Close,
                                    contentDescription = null
                                )
                            }
                        }
                    }

                    is ConnectionState.Connected -> {
                        Icon(
                            modifier = Modifier
                                .size(HostGameSize.iconSize)
                                .padding(start = mediumSize),
                            painter = AppIcons.Check,
                            contentDescription = null
                        )
                    }

                    ConnectionState.Loading,
                    is ConnectionState.Connecting -> {
                        CircularProgressIndicator(
                            modifier = Modifier.size(HostGameSize.iconSize),
                            color = contentColors,
                            trackColor = contentColors.copy(alpha = 0.2f),
                            strokeCap = StrokeCap.Round,
                        )
                    }

                    is ConnectionState.Error -> {
                        Icon(
                            modifier = Modifier
                                .size(HostGameSize.iconSize)
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
        }
    )
}

@Preview
@Composable
fun PreviewQueueScreen() {
    QueueScreen(
        state = QueueState().apply {
            devicesToConnect.addAll(
                listOf(
                    Device("1", "Device 1", ConnectionState.Idle),
                    Device("2", "Device 2", ConnectionState.Idle),
                    Device("3", "Device 3", ConnectionState.Idle),
                )
            )
            advertising = false
        },
        snackbarHostState = SnackbarHostState(),
        onIntent = {}
    )
}
