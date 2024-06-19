package g.sig.questweaver.common.ui.layouts

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
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
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.window.core.layout.WindowHeightSizeClass
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import g.sig.questweaver.common.ui.components.AdaptiveNavigationButton
import g.sig.questweaver.ui.AppTheme

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
    @Composable
    fun keyboardAsState(): State<Boolean> {
        val isImeVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0
        return rememberUpdatedState(isImeVisible)
    }

    val isKeyboardVisible by keyboardAsState()
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val shouldCenter = windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED ||
            windowSizeClass.windowHeightSizeClass != WindowHeightSizeClass.COMPACT
    val hideExtras = when (windowSizeClass.windowHeightSizeClass) {
        WindowHeightSizeClass.COMPACT -> !isKeyboardVisible
        else -> true
    }

    val adaptiveModifier =
        if (windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.COMPACT) {
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            AdaptiveContentLayout(
                windowSizeClass = windowSizeClass,
                isKeyboardVisible = isKeyboardVisible,
                shouldCenter = shouldCenter,
                decoration = decoration,
                content = content
            )

            AnimatedVisibility(
                hideExtras,
                modifier = adaptiveModifier.align(Alignment.End)
            ) {
                Row { navigation() }
            }
        }
    }
}

@Composable
private fun ColumnScope.AdaptiveContentLayout(
    windowSizeClass: WindowSizeClass,
    isKeyboardVisible: Boolean,
    shouldCenter: Boolean,
    decoration: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    if (windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.COMPACT) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedVisibility(visible = !isKeyboardVisible) {
                Box(contentAlignment = Alignment.Center) {
                    decoration()
                }
            }
            content()
        }
    } else {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = if (shouldCenter) {
                Alignment.CenterVertically
            } else {
                Alignment.Top
            },
            horizontalArrangement = Arrangement.Center
        ) {
            AnimatedVisibility(visible = !isKeyboardVisible) {
                Box(contentAlignment = Alignment.Center) {
                    decoration()
                }
            }
            content()
        }
    }
}

@Composable
@Preview(device = "spec:parent=pixel_5,orientation=landscape")
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
