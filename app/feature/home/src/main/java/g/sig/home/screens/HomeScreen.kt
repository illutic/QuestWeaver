package g.sig.home.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import g.sig.domain.entities.RecentGame
import g.sig.home.R
import g.sig.home.state.HomeIntent
import g.sig.home.state.HomeState
import g.sig.ui.AppIcons
import g.sig.ui.MediumRoundedShape
import g.sig.ui.components.Alert
import g.sig.ui.largeSize
import g.sig.ui.mediumSize

@Composable
internal fun HomeScreen(
    modifier: Modifier = Modifier,
    homeState: HomeState,
    onIntent: (HomeIntent) -> Unit
) {
    Column(
        modifier = modifier.padding(largeSize),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HomeScreenTopContent(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .weight(1f),
            state = homeState,
            onNavigateToHostGame = { onIntent(HomeIntent.NavigateToHost) },
            onNavigateToJoinGame = { onIntent(HomeIntent.NavigateToJoin) },
            onNavigateToGame = { gameId -> onIntent(HomeIntent.NavigateToGame(gameId)) }
        )
        HomeScreenBottomContent(
            modifier = Modifier
                .width(IntrinsicSize.Max)
                .padding(top = mediumSize),
            state = homeState,
            onNavigateToProfile = { onIntent(HomeIntent.NavigateToProfile) },
            onNavigateToSettings = { onIntent(HomeIntent.NavigateToSettings) },
            onNavigateToPermissions = { onIntent(HomeIntent.NavigateToPermissions) }
        )
    }
}

@Composable
private fun RecentGameCard(modifier: Modifier = Modifier, recentGame: RecentGame, onNavigateToGame: (String) -> Unit) {
    Surface(
        modifier = modifier,
        onClick = { onNavigateToGame(recentGame.id) },
        shape = MediumRoundedShape
    ) {
        Box {
            AsyncImage(
                modifier = Modifier.matchParentSize(),
                model = recentGame.imageUri,
                contentDescription = null,
                contentScale = ContentScale.Crop
            )

            Text(
                modifier = Modifier
                    .padding(mediumSize)
                    .align(Alignment.BottomStart),
                text = recentGame.title,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun HomeScreenRecentGamesCarousel(modifier: Modifier = Modifier, recentGames: List<RecentGame>, onNavigateToGame: (String) -> Unit) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(mediumSize),
    ) {
        items(recentGames) { recentGame ->
            RecentGameCard(
                modifier = Modifier.size(HomeScreenSize.gameCardSize),
                recentGame = recentGame,
                onNavigateToGame = onNavigateToGame
            )
        }
    }
}

@Composable
private fun HomeScreenTopContent(
    modifier: Modifier = Modifier,
    state: HomeState,
    onNavigateToHostGame: () -> Unit,
    onNavigateToJoinGame: () -> Unit,
    onNavigateToGame: (String) -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            modifier = Modifier.size(HomeScreenSize.graphicSize),
            model = R.drawable.graphic_3,
            contentDescription = null
        )

        Text(
            text = stringResource(R.string.home_title, state.userName),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = mediumSize),
            color = MaterialTheme.colorScheme.onBackground
        )

        HomeScreenRecentGamesCarousel(
            modifier = Modifier.padding(vertical = mediumSize),
            recentGames = state.recentGames,
            onNavigateToGame = onNavigateToGame
        )

        FilledTonalButton(
            modifier = Modifier.padding(mediumSize),
            onClick = onNavigateToHostGame
        ) {
            Text(text = stringResource(id = R.string.home_cta_1))
        }

        Button(
            onClick = onNavigateToJoinGame
        ) {
            Text(text = stringResource(id = R.string.home_cta_2))
        }
    }
}

@Composable
private fun HomeScreenBottomContent(
    modifier: Modifier = Modifier,
    state: HomeState,
    onNavigateToProfile: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToPermissions: () -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(mediumSize),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (!state.hasPermissions) {
            Alert(
                primaryColor = MaterialTheme.colorScheme.error,
                onClick = onNavigateToPermissions,
                trailingContent = {
                    Icon(painter = AppIcons.ChevronRight, contentDescription = "")
                },
                leadingContent = {
                    Icon(painter = AppIcons.Info, contentDescription = "")
                },
                content = {
                    Text(text = stringResource(id = R.string.home_permission_alert))
                }
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(mediumSize)
        ) {
            OutlinedButton(
                modifier = Modifier.weight(1f),
                onClick = onNavigateToProfile
            ) {
                Icon(painter = AppIcons.PersonOutline, contentDescription = "")
                Text(text = stringResource(id = R.string.home_profile))
            }

            OutlinedButton(
                modifier = Modifier.weight(1f),
                onClick = onNavigateToSettings
            ) {
                Icon(painter = AppIcons.SettingsOutline, contentDescription = "")
                Text(text = stringResource(id = R.string.home_settings))
            }
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    HomeScreen(
        homeState = HomeState().apply {
            userName = "John Doe"
            recentGames = listOf(
                RecentGame("1", "Game 1", ""),
                RecentGame("2", "Game 2", ""),
                RecentGame("3", "Game 3", "")
            )
        },
        onIntent = {}
    )
}
