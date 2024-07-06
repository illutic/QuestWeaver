package g.sig.questweaver.game.home.screens.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.RippleConfiguration
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import g.sig.questweaver.game.home.R
import g.sig.questweaver.ui.AppIcons
import g.sig.questweaver.ui.largeSize
import g.sig.questweaver.ui.xLargeSize
import kotlin.math.roundToInt

@Suppress("MagicNumber")
private val LinearGradientColors = listOf(
    0.0f to Color.Red,
    0.2f to Color.Yellow,
    0.35f to Color.Green,
    0.5f to Color.Cyan,
    0.65f to Color.Blue,
    0.8f to Color.Magenta,
    1.0f to Color.Red
)

private val GrayscaleColors = listOf(
    0.0f to Color.Black,
    1.0f to Color.White
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorButton(
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    CompositionLocalProvider(
        LocalRippleConfiguration provides RippleConfiguration(color = color)
    ) {
        OutlinedIconButton(onClick = onClick, modifier = modifier) {
            Icon(
                painter = AppIcons.Color,
                tint = color,
                contentDescription = null
            )
        }
    }
}

@Composable
fun ColorPicker(
    initialColor: Color,
    onDismiss: () -> Unit,
    onColorSelected: (Color) -> Unit,
) {
    var selectedColor by remember { mutableStateOf(initialColor) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onColorSelected(selectedColor)
                onDismiss()
            }) { Text(text = stringResource(R.string.ok)) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(text = stringResource(R.string.cancel)) }
        },
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = stringResource(R.string.select_color))
                Box(
                    modifier = Modifier
                        .size(largeSize)
                        .background(selectedColor, shape = CircleShape)
                )
            }
        },
        text = {
            Column {
                ColorGradient(
                    colors = LinearGradientColors,
                    onColorSelected = { selectedColor = it },
                    modifier = Modifier
                        .height(56.dp)
                        .fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(largeSize))

                ColorGradient(
                    colors = GrayscaleColors,
                    onColorSelected = { selectedColor = it },
                    modifier = Modifier
                        .height(56.dp)
                        .fillMaxWidth()
                )
            }
        }
    )
}

@Composable
fun ColorGradient(
    colors: List<ColorStop>,
    onColorSelected: (Color) -> Unit,
    modifier: Modifier = Modifier,
) {
    var handleOffsetX by remember { mutableFloatStateOf(0f) }
    val density = LocalDensity.current
    var dragging by remember { mutableStateOf(false) }

    BoxWithConstraints(modifier = modifier) {
        val maxWidth = with(density) { maxWidth.toPx() }
        val largeSizePx = with(density) { largeSize.toPx() }

        fun selectColor(x: Float) {
            val relativeX = x / (maxWidth - largeSizePx)
            val (nearestStartColor, nearestEndColor) = findColorRange(relativeX, colors)

            val normalizedX = (relativeX - nearestStartColor.first) /
                    (nearestEndColor.first - nearestStartColor.first)

            val actualColor = lerp(
                nearestStartColor.second,
                nearestEndColor.second,
                normalizedX
            )

            onColorSelected(actualColor)
        }

        Canvas(
            modifier = Modifier
                .matchParentSize()
                .pointerInput(dragging) {
                    if (!dragging) {
                        detectTapGestures {
                            handleOffsetX = it.x.coerceIn(0f, maxWidth - largeSizePx)
                            selectColor(handleOffsetX)
                        }
                    }
                }
        ) {
            val gradient = colors.toTypedArray()
            val width = size.width

            drawRoundRect(
                brush = Brush.linearGradient(
                    colorStops = gradient,
                    start = Offset(0f, 0f),
                    end = Offset(width, 0f)
                ),
                cornerRadius = CornerRadius(xLargeSize.value),
            )
        }

        ColorPickerHandle(
            modifier = Modifier
                .height(maxHeight)
                .width(largeSize)
                .offset { IntOffset(handleOffsetX.roundToInt(), 0) },
            onHandleDragged = { x ->
                val offset = (handleOffsetX + x).coerceIn(0f, maxWidth - largeSizePx)
                handleOffsetX = offset
                selectColor(handleOffsetX)
            },
            onHandleDragStarted = { dragging = true },
            onHandleDragEnded = { dragging = false }
        )
    }
}

@Composable
fun ColorPickerHandle(
    onHandleDragStarted: (offset: Offset) -> Unit,
    onHandleDragEnded: () -> Unit,
    onHandleDragged: (Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .border(2.dp, Color.White, CircleShape)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = onHandleDragStarted,
                    onDragCancel = onHandleDragEnded,
                    onDragEnd = onHandleDragEnded
                ) { change, dragAmount ->
                    change.consume()
                    onHandleDragged(dragAmount.x)
                }
            }
    )
}

private typealias ColorStop = Pair<Float, Color>

private fun findColorRange(
    x: Float,
    colorStops: List<ColorStop> = LinearGradientColors
): Pair<ColorStop, ColorStop> {
    for (i in 0 until colorStops.size - 1) {
        val startColorStop = colorStops[i]
        val endColorStop = colorStops[i + 1]
        if (x >= startColorStop.first && x < endColorStop.first) {
            return startColorStop to endColorStop
        }
    }

    return colorStops.first() to colorStops.last()
}

@Composable
@Preview
fun ColorPickerPreview() {
    var selectedColor by remember { mutableStateOf(Color.Red) }

    Box(
        modifier = Modifier
            .background(selectedColor)
            .size(50.dp)
    )

    ColorPicker(
        initialColor = Color.Red,
        onDismiss = {},
        onColorSelected = {
            selectedColor = it
        },
    )
}
