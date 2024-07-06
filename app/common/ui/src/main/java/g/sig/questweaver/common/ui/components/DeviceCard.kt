package g.sig.questweaver.common.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import g.sig.questweaver.domain.entities.common.Device
import g.sig.questweaver.domain.entities.states.ConnectionState
import g.sig.questweaver.ui.AppIcons
import g.sig.questweaver.ui.defaultAnimationSpec
import g.sig.questweaver.ui.defaultContentTransform
import g.sig.questweaver.ui.largeSize
import g.sig.questweaver.ui.mediumSize


@Composable
fun DeviceCard(
    device: Device,
    modifier: Modifier = Modifier,
    onDeviceClicked: ((Device) -> Unit)? = null,
    trailingContent: (@Composable () -> Unit)? = null
) {
    val contentColor by deviceCardContentColors(device)
    val backgroundColor by deviceCardBackgroundColors(device)

    Surface(modifier = modifier,
        tonalElevation = mediumSize,
        color = backgroundColor,
        contentColor = contentColor,
        border = BorderStroke(1.dp, contentColor.copy(alpha = 0.5f)),
        shape = MaterialTheme.shapes.medium,
        enabled = device.connectionState !is ConnectionState.Connected && onDeviceClicked != null,
        onClick = { onDeviceClicked?.invoke(device) }) {
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

            AnimatedContent(targetState = device.connectionState,
                label = "icon",
                transitionSpec = { defaultContentTransform }) { state ->
                when (state) {
                    is ConnectionState.Found, ConnectionState.Idle ->
                        if (trailingContent != null) {
                            trailingContent()
                        } else {
                            DeviceCardIcon(AppIcons.ConnectingDevice)
                        }

                    is ConnectionState.Connected -> DeviceCardIcon(AppIcons.Check)

                    ConnectionState.Loading, is ConnectionState.Connecting -> LoadingIndicator(
                        contentColor
                    )

                    is ConnectionState.Error -> DeviceCardIcon(AppIcons.Close)
                }
            }
        }
    }
}

@Composable
private fun LoadingIndicator(contentColors: Color) {
    CircularProgressIndicator(
        modifier = Modifier.size(24.dp),
        color = contentColors,
        trackColor = contentColors.copy(alpha = 0.2f),
        strokeCap = StrokeCap.Round,
    )
}

@Composable
private fun DeviceCardIcon(painter: Painter) {
    Icon(
        modifier = Modifier
            .size(24.dp)
            .padding(start = mediumSize),
        painter = painter,
        contentDescription = null
    )
}

@Composable
private fun deviceCardContentColors(device: Device) = animateColorAsState(
    targetValue = when (device.connectionState) {
        is ConnectionState.Found,
        ConnectionState.Idle -> contentColorFor(MaterialTheme.colorScheme.surface)

        is ConnectionState.Connected -> contentColorFor(MaterialTheme.colorScheme.primaryContainer)

        ConnectionState.Loading,
        is ConnectionState.Connecting -> contentColorFor(
            MaterialTheme.colorScheme.secondaryContainer
        )

        is ConnectionState.Error -> contentColorFor(MaterialTheme.colorScheme.errorContainer)
    },
    animationSpec = defaultAnimationSpec(),
    label = "content color"
)

@Composable
private fun deviceCardBackgroundColors(device: Device) = animateColorAsState(
    targetValue = when (device.connectionState) {
        is ConnectionState.Found, ConnectionState.Idle -> MaterialTheme.colorScheme.surface

        is ConnectionState.Connected -> MaterialTheme.colorScheme.primaryContainer

        ConnectionState.Loading,
        is ConnectionState.Connecting -> MaterialTheme.colorScheme.secondaryContainer

        is ConnectionState.Error -> MaterialTheme.colorScheme.errorContainer
    },
    animationSpec = defaultAnimationSpec(),
    label = "background color"
)
