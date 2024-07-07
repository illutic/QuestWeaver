package g.sig.questweaver.common.ui.layouts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
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
import g.sig.questweaver.common.ui.components.AdaptiveNavigationButton
import g.sig.questweaver.ui.AppTheme

@Composable
fun ScreenScaffold(
    modifier: Modifier = Modifier,
    navigation: @Composable RowScope.() -> Unit = {},
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    containerColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = contentColorFor(containerColor),
    contentWindowInsets: WindowInsets = ScaffoldDefaults.contentWindowInsets,
    content: @Composable () -> Unit,
) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass

    val adaptiveModifier =
        if (windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.COMPACT) {
            Modifier.fillMaxWidth()
        } else {
            Modifier
                .width(IntrinsicSize.Max)
                .displayCutoutPadding()
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
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(padding),
        ) {
            val contentModifier =
                Modifier
                    .fillMaxWidth()
                    .weight(1f)

            if (windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.COMPACT) {
                Column(
                    modifier = contentModifier,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    content()
                }
            } else {
                Row(
                    modifier =
                        contentModifier
                            .displayCutoutPadding(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    content()
                }
            }

            Row(
                modifier = adaptiveModifier.align(Alignment.End),
            ) { navigation() }
        }
    }
}

@Composable
@Preview(device = "spec:parent=pixel_5,orientation=landscape")
private fun AdaptiveScaffoldPreview() {
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
            },
        ) {
            Text("Hello")
        }
    }
}
