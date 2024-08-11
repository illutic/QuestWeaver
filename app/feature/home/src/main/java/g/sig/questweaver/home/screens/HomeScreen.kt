package g.sig.questweaver.home.screens

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import g.sig.questweaver.common.ui.components.CenteredProgressBar
import g.sig.questweaver.common.ui.components.ImageWithPlaceholder
import g.sig.questweaver.common.ui.components.PermissionsAlert
import g.sig.questweaver.common.ui.components.drawAnnotations
import g.sig.questweaver.common.ui.layouts.ScreenScaffold
import g.sig.questweaver.domain.entities.blocks.Uri
import g.sig.questweaver.domain.entities.common.Game
import g.sig.questweaver.domain.entities.states.GameState
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
                    animationScope = animationScope,
                    onNavigateToProfile = { onIntent(HomeIntent.NavigateToProfile) },
                    onNavigateToSettings = { onIntent(HomeIntent.NavigateToSettings) },
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
                    navigationPadding = it,
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
    navigationPadding: PaddingValues = PaddingValues(),
    onIntent: (HomeIntent) -> Unit,
) {
    val scrollState = rememberScrollState()

    ImageWithPlaceholder(
        modifier = Modifier.verticalScroll(scrollState),
        size = HomeScreenSize.imageSize,
        model = R.drawable.graphic_3,
        contentDescription = null,
    )

    HomeScreenTopContent(
        modifier =
            Modifier
                .verticalScroll(scrollState)
                .padding(navigationPadding)
                .padding(horizontal = largeSize),
        state = homeState,
        animationScope = animationScope,
        onNavigateToPermissions = { onIntent(HomeIntent.NavigateToPermissions) },
        onNavigateToGame = { gameId -> onIntent(HomeIntent.NavigateToGame(gameId)) },
        onNavigateToHostGame = { onIntent(HomeIntent.NavigateToHost) },
        onNavigateToJoinGame = { onIntent(HomeIntent.NavigateToJoin) },
    )
}

@Composable
private fun RecentGameCard(
    modifier: Modifier = Modifier,
    recentGame: Game,
    recentGameState: GameState?,
    onNavigateToGame: (String) -> Unit,
) {
    Surface(
        modifier = modifier,
        onClick = { onNavigateToGame(recentGame.gameId) },
        tonalElevation = smallSize,
        shape = MediumRoundedShape,
    ) {
        val textMeasurer = rememberTextMeasurer()
        val textStyle = LocalTextStyle.current
        val context = LocalContext.current

        Box {
            Canvas(modifier = Modifier.matchParentSize()) {
                drawAnnotations(
                    recentGameState?.annotations.orEmpty(),
                    textMeasurer,
                    textStyle,
                    context,
                )
            }

            Row(
                modifier =
                    Modifier
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.95f))
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min)
                        .padding(horizontal = largeSize, vertical = mediumSize)
                        .align(Alignment.BottomStart),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = recentGame.title,
                    style = MaterialTheme.typography.bodyMedium,
                )

                Icon(painter = AppIcons.ChevronRight, contentDescription = "Join recent game")
            }
        }
    }
}

@Composable
private fun HomeScreenRecentGamesCarousel(
    modifier: Modifier = Modifier,
    recentGames: Map<Game, GameState?>,
    onNavigateToGame: (String) -> Unit,
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(mediumSize),
    ) {
        items(recentGames.keys.toList()) { recentGame ->
            RecentGameCard(
                modifier =
                    Modifier
                        .width(HomeScreenSize.gameCardSize)
                        .height(HomeScreenSize.gameCardSize * 0.75f),
                recentGame = recentGame,
                recentGameState = recentGames[recentGame] ?: return@items,
                onNavigateToGame = onNavigateToGame,
            )
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun HomeScreenTopContent(
    state: HomeState.Loaded,
    animationScope: AnimatedContentScope,
    modifier: Modifier = Modifier,
    onNavigateToHostGame: () -> Unit,
    onNavigateToJoinGame: () -> Unit,
    onNavigateToGame: (String) -> Unit,
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
            modifier =
                Modifier
                    .width(HomeScreenSize.gameCardSize * 1.5f)
                    .padding(vertical = mediumSize),
            recentGames = state.recentGames,
            onNavigateToGame = onNavigateToGame,
        )

        HomeScreenButtons(
            animationScope = animationScope,
            onNavigateToHostGame = onNavigateToHostGame,
            onNavigateToJoinGame = onNavigateToJoinGame,
        )

        if (permissionsState?.allPermissionsGranted == false) {
            PermissionsAlert(onClick = onNavigateToPermissions)
        }
    }
}

@Composable
private fun HomeScreenBottomContent(
    modifier: Modifier = Modifier,
    animationScope: AnimatedContentScope,
    onNavigateToProfile: () -> Unit,
    onNavigateToSettings: () -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(mediumSize),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
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

@Composable
private fun HomeScreenButtons(
    animationScope: AnimatedContentScope,
    modifier: Modifier = Modifier,
    onNavigateToHostGame: () -> Unit,
    onNavigateToJoinGame: () -> Unit,
) {
    Row(modifier = modifier) {
        FilledTonalButton(
            modifier = Modifier.sharedBounds(SharedElementKeys.HOST_KEY, animationScope),
            shape =
                CircleShape.copy(
                    bottomEnd = CornerSize(smallSize),
                    topEnd = CornerSize(smallSize),
                ),
            onClick = onNavigateToHostGame,
        ) {
            Text(
                text = stringResource(id = R.string.home_cta_1),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
        }

        Spacer(modifier = Modifier.width(smallSize))

        Button(
            modifier = Modifier.sharedBounds(SharedElementKeys.JOIN_KEY, animationScope),
            shape =
                CircleShape.copy(
                    bottomStart = CornerSize(smallSize),
                    topStart = CornerSize(smallSize),
                ),
            onClick = onNavigateToJoinGame,
        ) {
            Text(
                text = stringResource(id = R.string.home_cta_2),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
        }
    }
}

@SuppressLint("UnusedContentLambdaTargetStateParameter")
@Preview
@Composable
private fun HomeScreenPreview() {
    val games =
        listOf(
            Game("1", "Game 1", "", Uri("https://example.com"), 0, 0),
            Game("2", "Game 2", "", Uri("https://example.com"), 0, 0),
            Game("3", "Game 3", "", Uri("https://example.com"), 0, 0),
        )

    val gameWithStates = games.associateWith { GameState(it.gameId) }

    AnimatedContent(true, label = "home_preview") {
        HomeScreen(
            homeState =
                HomeState.Loaded(
                    userName = "John Doe",
                    permissions = emptyList(),
                    recentGames = gameWithStates,
                ),
            animationScope = this,
            onIntent = {},
        )
    }
}
