package g.sig.questweaver.home.screens

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import g.sig.questweaver.common.ui.components.CenteredProgressBar
import g.sig.questweaver.common.ui.components.ImageWithPlaceholder
import g.sig.questweaver.common.ui.components.PermissionsAlert
import g.sig.questweaver.common.ui.layouts.ScreenScaffold
import g.sig.questweaver.domain.entities.blocks.Uri
import g.sig.questweaver.domain.entities.common.Game
import g.sig.questweaver.home.R
import g.sig.questweaver.home.state.HomeIntent
import g.sig.questweaver.home.state.HomeState
import g.sig.questweaver.navigation.SharedElementKeys
import g.sig.questweaver.ui.AppIcons
import g.sig.questweaver.ui.MediumRoundedShape
import g.sig.questweaver.ui.largeSize
import g.sig.questweaver.ui.mediumSize
import g.sig.questweaver.ui.sharedBounds
import g.sig.questweaver.ui.smallSize

@Composable
internal fun HomeScreen(
    homeState: HomeState,
    animationScope: AnimatedContentScope,
    onIntent: (HomeIntent) -> Unit,
) {
    ScreenScaffold(
        modifier = Modifier.fillMaxSize(),
        navigation = {
            if (homeState is HomeState.Loaded) {
                HomeScreenBottomContent(
                    modifier = Modifier.padding(horizontal = largeSize),
                    state = homeState,
                    animationScope = animationScope,
                    onNavigateToProfile = { onIntent(HomeIntent.NavigateToProfile) },
                    onNavigateToSettings = { onIntent(HomeIntent.NavigateToSettings) },
                    onNavigateToPermissions = { onIntent(HomeIntent.NavigateToPermissions) },
                )
            }
        },
    ) {
        when (homeState) {
            HomeState.Idle -> {}
            is HomeState.Loaded ->
                HomeScreenContent(
                    homeState = homeState,
                    animationScope = animationScope,
                    onIntent = onIntent,
                )

            HomeState.Loading -> CenteredProgressBar()
        }
    }
}

@Composable
private fun HomeScreenContent(
    homeState: HomeState.Loaded,
    animationScope: AnimatedContentScope,
    onIntent: (HomeIntent) -> Unit,
) {
    ImageWithPlaceholder(
        modifier = Modifier.size(HomeScreenSize.imageSize),
        model = R.drawable.graphic_3,
        contentDescription = null,
    )

    HomeScreenTopContent(
        modifier = Modifier.padding(horizontal = largeSize),
        state = homeState,
        animationScope = animationScope,
        onNavigateToHostGame = { onIntent(HomeIntent.NavigateToHost) },
        onNavigateToJoinGame = { onIntent(HomeIntent.NavigateToJoin) },
        onNavigateToGame = { gameId -> onIntent(HomeIntent.NavigateToGame(gameId)) },
    )
}

@Composable
private fun RecentGameCard(
    modifier: Modifier = Modifier,
    recentGame: Game,
    onNavigateToGame: (String) -> Unit,
) {
    Surface(
        modifier = modifier,
        onClick = { onNavigateToGame(recentGame.gameId) },
        tonalElevation = smallSize,
        shape = MediumRoundedShape,
    ) {
        Box {
            ImageWithPlaceholder(
                modifier = Modifier.matchParentSize(),
                model = recentGame.imageUri?.value,
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )

            Text(
                modifier =
                    Modifier
                        .padding(mediumSize)
                        .align(Alignment.BottomStart),
                text = recentGame.title,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Composable
private fun HomeScreenRecentGamesCarousel(
    modifier: Modifier = Modifier,
    recentGames: List<Game>,
    onNavigateToGame: (String) -> Unit,
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(mediumSize),
    ) {
        items(recentGames) { recentGame ->
            RecentGameCard(
                modifier = Modifier.size(HomeScreenSize.gameCardSize),
                recentGame = recentGame,
                onNavigateToGame = onNavigateToGame,
            )
        }
    }
}

@Composable
private fun HomeScreenTopContent(
    modifier: Modifier = Modifier,
    state: HomeState.Loaded,
    animationScope: AnimatedContentScope,
    onNavigateToHostGame: () -> Unit,
    onNavigateToJoinGame: () -> Unit,
    onNavigateToGame: (String) -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.home_title, state.userName),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = mediumSize),
            color = MaterialTheme.colorScheme.onBackground,
        )

        HomeScreenRecentGamesCarousel(
            modifier = Modifier.padding(vertical = mediumSize),
            recentGames = state.recentGames,
            onNavigateToGame = onNavigateToGame,
        )

        FilledTonalButton(
            modifier =
                Modifier
                    .padding(mediumSize)
                    .sharedBounds(SharedElementKeys.HOST_KEY, animationScope),
            onClick = onNavigateToHostGame,
        ) {
            Text(text = stringResource(id = R.string.home_cta_1))
        }

        Button(
            modifier = Modifier.sharedBounds(SharedElementKeys.JOIN_KEY, animationScope),
            onClick = onNavigateToJoinGame,
        ) {
            Text(text = stringResource(id = R.string.home_cta_2))
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun HomeScreenBottomContent(
    modifier: Modifier = Modifier,
    state: HomeState.Loaded,
    animationScope: AnimatedContentScope,
    onNavigateToProfile: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToPermissions: () -> Unit,
) {
    val permissionsState =
        if (!LocalInspectionMode.current) {
            rememberMultiplePermissionsState(state.permissions)
        } else {
            null
        }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(mediumSize),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (permissionsState?.allPermissionsGranted == false) {
            PermissionsAlert(onClick = onNavigateToPermissions)
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(mediumSize),
        ) {
            OutlinedButton(
                modifier =
                    Modifier
                        .weight(1f)
                        .sharedBounds(SharedElementKeys.PROFILE_KEY, animationScope),
                onClick = onNavigateToProfile,
            ) {
                Icon(painter = AppIcons.PersonOutline, contentDescription = "")
                Text(text = stringResource(id = R.string.home_profile))
            }

            OutlinedButton(
                modifier =
                    Modifier
                        .weight(1f)
                        .sharedBounds(SharedElementKeys.SETTINGS_KEY, animationScope),
                onClick = onNavigateToSettings,
            ) {
                Icon(painter = AppIcons.SettingsOutline, contentDescription = "")
                Text(text = stringResource(id = R.string.home_settings))
            }
        }
    }
}

@SuppressLint("UnusedContentLambdaTargetStateParameter")
@Preview
@Composable
private fun HomeScreenPreview() {
    AnimatedContent(true, label = "home_preview") {
        HomeScreen(
            homeState =
                HomeState.Loaded(
                    userName = "John Doe",
                    permissions = emptyList(),
                    recentGames =
                        listOf(
                            Game("1", "Game 1", "", Uri("https://example.com"), 0, 0),
                            Game("2", "Game 2", "", Uri("https://example.com"), 0, 0),
                            Game("3", "Game 3", "", Uri("https://example.com"), 0, 0),
                        ),
                ),
            animationScope = this,
            onIntent = {},
        )
    }
}
