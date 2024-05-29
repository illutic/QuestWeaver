package g.sig.common.ui.components

import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.window.core.layout.WindowHeightSizeClass
import coil.compose.AsyncImage
import g.sig.ui.AppIcons

@Composable
fun AdaptiveImage(
    model: Any?,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    contentScale: ContentScale = ContentScale.Fit
) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass

    val adaptiveModifier = if (windowSizeClass.windowHeightSizeClass == WindowHeightSizeClass.COMPACT) {
        Modifier.wrapContentSize()
    } else {
        Modifier
    }

    AsyncImage(
        model = model,
        contentDescription = contentDescription,
        contentScale = contentScale,
        modifier = modifier.then(adaptiveModifier),
        placeholder = if (LocalInspectionMode.current) {
            AppIcons.Refresh
        } else null
    )
}