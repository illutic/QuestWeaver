package g.sig.questweaver.common.ui.mappers

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.toSize
import g.sig.questweaver.domain.entities.blocks.Size
import androidx.compose.ui.geometry.Size as ComposeSize

@Suppress("TopLevelPropertyNaming")
private const val STROKE_WIDTH_FACTOR = 0.2f

@Suppress("TopLevelPropertyNaming")
private const val DEFAULT_TEXT_SIZE_PERCENT = .4f

fun Size.getStrokeWidth(canvasSize: ComposeSize) = Stroke(width * (canvasSize.width * STROKE_WIDTH_FACTOR))

fun Size.toSp(canvasSize: ComposeSize) = width * canvasSize.width * DEFAULT_TEXT_SIZE_PERCENT

fun ComposeSize.toSize(canvasSize: ComposeSize) = Size(width / canvasSize.width, height / canvasSize.height)

fun IntSize.toSize(canvasSize: IntSize) = toSize().toSize(canvasSize.toSize())

fun Size.toOffset(canvasSize: ComposeSize) = Offset(width * canvasSize.width, height * canvasSize.height)
