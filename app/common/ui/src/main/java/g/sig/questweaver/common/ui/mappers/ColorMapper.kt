package g.sig.questweaver.common.ui.mappers

import g.sig.questweaver.domain.entities.blocks.Color
import androidx.compose.ui.graphics.Color as ComposeColor

fun ComposeColor.toColor() = Color(value)

fun Color.toComposeColor() = ComposeColor(value)
