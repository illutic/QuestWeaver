package g.sig.questweaver.common.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import g.sig.questweaver.ui.MediumRoundedShape

@Composable
fun Alert(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    borderWidth: Dp = 1.dp,
    primaryColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    shape: Shape = MediumRoundedShape,
    innerPadding: Dp = 16.dp,
    contentPadding: Dp = 10.dp,
    content: @Composable (() -> Unit)? = null,
    leadingContent: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null
) {
    Surface(
        modifier = modifier,
        onClick = onClick,
        color = Color.Transparent,
        border = BorderStroke(borderWidth, primaryColor),
        shape = shape
    ) {
        Row(
            modifier = Modifier.padding(innerPadding),
            horizontalArrangement = Arrangement.spacedBy(contentPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CompositionLocalProvider(LocalContentColor provides primaryColor) {
                leadingContent?.invoke()
            }

            CompositionLocalProvider(LocalContentColor provides contentColor) {
                Box(
                    modifier = Modifier.weight(1f, false),
                    propagateMinConstraints = true
                ) {
                    content?.invoke()
                }
                trailingContent?.invoke()
            }
        }
    }
}
