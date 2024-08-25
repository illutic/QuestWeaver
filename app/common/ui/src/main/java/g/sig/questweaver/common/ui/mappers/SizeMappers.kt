package g.sig.questweaver.common.ui.mappers

import androidx.compose.ui.graphics.drawscope.Stroke
import g.sig.questweaver.domain.entities.blocks.Size
import androidx.compose.ui.geometry.Size as ComposeSize

@Suppress("TopLevelPropertyNaming")
private const val STROKE_WIDTH_FACTOR = 0.2f

fun Size.getStrokeWidth(canvasSize: ComposeSize) = Stroke(width * (canvasSize.width * STROKE_WIDTH_FACTOR))
