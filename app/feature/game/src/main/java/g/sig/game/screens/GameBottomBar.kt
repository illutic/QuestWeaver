package g.sig.game.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.tooling.preview.Preview
import g.sig.game.ai.navigation.GameAiRoute
import g.sig.game.chat.navigation.GameChatRoute
import g.sig.game.home.navigation.GameHomeRoute
import g.sig.navigation.DecoratedRoute
import g.sig.navigation.painter
import g.sig.ui.AppTheme
import g.sig.ui.largeSize
import g.sig.ui.smallSize

@Composable
internal inline fun GameBottomBar(
    modifier: Modifier = Modifier,
    selectedRoute: DecoratedRoute,
    routes: List<DecoratedRoute>,
    crossinline onItemClick: (DecoratedRoute) -> Unit
) {
    BottomAppBar(modifier) {
        routes.forEach { route ->
            GameBottomBarItem(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                route = route,
                selectedRoute = selectedRoute,
                onItemClick = onItemClick
            )
        }
    }
}

@Composable
private inline fun GameBottomBarItem(
    modifier: Modifier = Modifier,
    animationDuration: Int = 700,
    route: DecoratedRoute,
    selectedRoute: DecoratedRoute,
    crossinline onItemClick: (DecoratedRoute) -> Unit
) {
    val isSelected by remember(selectedRoute) { derivedStateOf { route == selectedRoute } }

    Box(
        modifier
            .clickable(indication = null, interactionSource = remember { MutableInteractionSource() }) { onItemClick(route) },
        contentAlignment = Alignment.Center
    ) {
        AnimatedContent(
            targetState = isSelected,
            label = "selected route animation",
            transitionSpec = { fadeIn(tween(animationDuration)) togetherWith fadeOut(tween(animationDuration)) }
        ) { isSelected ->
            val backgroundColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
            val contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(smallSize)
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            color = backgroundColor,
                            shape = CircleShape
                        )
                ) {
                    Image(
                        modifier = Modifier.padding(vertical = smallSize, horizontal = largeSize),
                        painter = if (isSelected) route.selectedIcon.painter else route.unselectedIcon.painter,
                        colorFilter = ColorFilter.tint(contentColor),
                        contentDescription = route.label + " icon"
                    )
                }

                if (isSelected) {
                    Text(
                        text = route.label,
                        color = contentColor,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun GameBottomBarPreview() {
    val routes = remember {
        listOf(
            GameHomeRoute("Home"),
            GameChatRoute("Chat"),
            GameAiRoute("AI")
        )
    }
    var selectedRoute by remember { mutableStateOf(routes.first()) }

    AppTheme {
        GameBottomBar(
            onItemClick = { selectedRoute = it },
            selectedRoute = selectedRoute,
            routes = routes
        )
    }
}
