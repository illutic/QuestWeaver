package g.sig.questweaver.common.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.window.core.layout.WindowWidthSizeClass

@Composable
fun AdaptiveNavigationButton(
    modifier: Modifier = Modifier,
    content: @Composable (() -> Unit)
) {
    val windowClass = currentWindowAdaptiveInfo().windowSizeClass
    val adaptiveModifier = when (windowClass.windowWidthSizeClass) {
        WindowWidthSizeClass.COMPACT -> modifier.fillMaxWidth()
        else -> modifier
    }

    Box(adaptiveModifier, propagateMinConstraints = true) { content() }
}
