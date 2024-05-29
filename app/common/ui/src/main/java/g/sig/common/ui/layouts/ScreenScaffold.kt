package g.sig.common.ui.layouts

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.window.core.layout.WindowWidthSizeClass
import g.sig.common.ui.components.AdaptiveNavigationButton
import g.sig.ui.AppTheme

@Composable
fun ScreenScaffold(
    modifier: Modifier = Modifier,
    navigation: @Composable RowScope.() -> Unit = {},
    decoration: @Composable () -> Unit = {},
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    containerColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = contentColorFor(containerColor),
    contentWindowInsets: WindowInsets = ScaffoldDefaults.contentWindowInsets,
    content: @Composable () -> Unit
) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val adaptiveModifier = if (windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.COMPACT) {
        Modifier.fillMaxWidth()
    } else {
        Modifier.width(IntrinsicSize.Max)
    }

    Scaffold(
        modifier = modifier,
        topBar = topBar,
        bottomBar = bottomBar,
        snackbarHost = snackbarHost,
        floatingActionButton = floatingActionButton,
        floatingActionButtonPosition = floatingActionButtonPosition,
        containerColor = containerColor,
        contentColor = contentColor,
        contentWindowInsets = contentWindowInsets,
    ) { padding ->
        Row(modifier = Modifier.padding(padding)) {
            if (windowSizeClass.windowWidthSizeClass != WindowWidthSizeClass.COMPACT) {
                Box(
                    modifier = Modifier.fillMaxHeight(),
                    contentAlignment = Alignment.Center,
                    propagateMinConstraints = true
                ) {
                    decoration()
                }
            }
            Column {
                if (windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.COMPACT) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center,
                        propagateMinConstraints = true
                    ) {
                        decoration()
                    }
                }
                Box(
                    modifier = Modifier.weight(1f),
                    propagateMinConstraints = true,
                    contentAlignment = Alignment.Center
                ) {
                    Box(contentAlignment = Alignment.Center) { content() }
                }
                Row(modifier = adaptiveModifier.align(Alignment.End)) { navigation() }
            }
        }
    }
}

@Composable
@Preview
fun AdaptiveScaffoldPreview() {
    AppTheme {
        ScreenScaffold(
            navigation = {
                Row {
                    AdaptiveNavigationButton(
                        modifier = Modifier.weight(1f, false),
                    ) { OutlinedButton(onClick = { }) { Text("Navigation") } }
                    AdaptiveNavigationButton(
                        modifier = Modifier.weight(1f, false),
                    ) { Button(onClick = { }) { Text("Navigation") } }
                }
            }
        ) {
            Text("Hello")
        }
    }
}