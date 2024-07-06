package g.sig.questweaver.common.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.window.core.layout.WindowWidthSizeClass

@Composable
fun CenteredProgressBar() {
    Box(modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
    }
}

@Composable
fun AdaptiveProgressBar(modifier: Modifier = Modifier) {
    val adaptiveWindowClass = currentWindowAdaptiveInfo().windowSizeClass
    val windowWidthSizeClass = adaptiveWindowClass.windowWidthSizeClass

    if (windowWidthSizeClass == WindowWidthSizeClass.COMPACT) {
        LinearProgressIndicator(modifier = modifier)
    } else {
        Box(modifier = modifier) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}