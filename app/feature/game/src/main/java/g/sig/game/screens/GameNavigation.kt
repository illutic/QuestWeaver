package g.sig.game.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import g.sig.game.ai.navigation.GameAiRoute
import g.sig.game.chat.navigation.GameChatRoute
import g.sig.game.home.navigation.GameHomeRoute
import g.sig.navigation.DecoratedRoute
import g.sig.navigation.painter
import g.sig.ui.AppTheme

@Composable
internal fun GameNavigation(
    selectedRoute: DecoratedRoute,
    routes: List<DecoratedRoute>,
    onItemClick: (DecoratedRoute) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {}
) {
    NavigationSuiteScaffold(
        modifier = modifier,
        navigationSuiteItems = {
            routes.forEach { route ->
                val isSelected = selectedRoute::class.isInstance(route)
                item(
                    selected = isSelected,
                    alwaysShowLabel = false,
                    onClick = { onItemClick(route) },
                    icon = {
                        Icon(
                            painter = if (isSelected) route.selectedIcon.painter else route.unselectedIcon.painter,
                            contentDescription = route.label + " icon"
                        )
                    },
                    label = { Text(text = route.label) }
                )
            }
        },
        content = content
    )
}

@Preview
@Composable
private fun GameNavigationPreview() {
    val routes = remember {
        listOf(
            GameHomeRoute("Home"),
            GameChatRoute("Chat"),
            GameAiRoute("AI")
        )
    }
    var selectedRoute by remember { mutableStateOf(routes.first()) }

    AppTheme {
        GameNavigation(
            modifier = Modifier.fillMaxSize(),
            onItemClick = { selectedRoute = it },
            selectedRoute = selectedRoute,
            routes = routes
        ) {
            Box(
                modifier = Modifier
                    .safeContentPadding()
                    .fillMaxSize()
            )
        }
    }
}
