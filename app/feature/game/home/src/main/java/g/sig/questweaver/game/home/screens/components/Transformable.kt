package g.sig.questweaver.game.home.screens.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
import g.sig.questweaver.ui.AppTheme

@Composable
inline fun Transformable(
    modifier: Modifier = Modifier,
    allowTransformations: Boolean = true,
    enableClick: Boolean = true,
    initialOffset: Offset = Offset.Zero,
    initialAngle: Float = 0f,
    initialZoom: Float = 1f,
    crossinline onRotationChange: (Float) -> Unit = {},
    crossinline onScaleChange: (Float) -> Unit = {},
    crossinline onPositionChange: (Offset) -> Unit = {},
    crossinline onClick: () -> Unit = {},
    border: BorderStroke? = null,
    content: @Composable () -> Unit = {},
) {
    var offset by remember(initialOffset) { mutableStateOf(initialOffset) }
    var angle by remember(initialAngle) { mutableFloatStateOf(initialAngle) }
    var zoom by remember(initialZoom) { mutableFloatStateOf(initialZoom) }

    val state =
        rememberTransformableState { zoomChange, offsetChange, rotationChange ->
            zoom *= zoomChange
            angle += rotationChange
            offset += offsetChange

            onScaleChange(zoom)
            onPositionChange(offset)
            onRotationChange(angle)
        }

    Box(
        modifier
            .offset { offset.round() }
            .scale(zoom)
            .transformable(state, enabled = allowTransformations)
            .rotate(angle)
            .clickable(
                interactionSource = null,
                indication = null,
                enabled = enableClick,
                onClick = { onClick() },
            ).then(
                if (border != null) {
                    Modifier.border(border)
                } else {
                    Modifier
                },
            ),
    ) {
        content()
    }
}

@Composable
@Preview
private fun HomeTextFieldPreview() {
    var text by remember { mutableStateOf("asdfasdf") }
    AppTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            Transformable(
                modifier = Modifier.height(48.dp),
            ) {
                BasicTextField(
                    value = text,
                    onValueChange = { text = it },
                )
            }
        }
    }
}
