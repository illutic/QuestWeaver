package g.sig.join_game.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import g.sig.common.ui.PermissionsAlert
import g.sig.domain.entities.Game
import g.sig.join_game.R
import g.sig.join_game.state.JoinGameIntent
import g.sig.join_game.state.JoinGameState
import g.sig.ui.AppIcons
import g.sig.ui.largeSize
import g.sig.ui.mediumSize

@Composable
internal fun JoinGameScreen(state: JoinGameState, onIntent: (JoinGameIntent) -> Unit) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { JoinGameTopBar { onIntent(JoinGameIntent.Back) } },
    ) { padding ->
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = largeSize),
            verticalArrangement = spacedBy(largeSize),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                AsyncImage(
                    modifier = Modifier.width(360.dp),
                    model = R.drawable.graphic_9,
                    contentDescription = ""
                )
            }
            if (state.isLoading && state.hasPermissions) {
                item {
                    LinearProgressIndicator(Modifier.width(IntrinsicSize.Max))
                }
            }
            if (!state.hasPermissions) {
                item {
                    PermissionsAlert(onClick = { onIntent(JoinGameIntent.NavigateToPermissions) })
                }
            }
            if (state.games.isNotEmpty()) {
                items(state.games) { game ->
                    JoinGameCard(game = game) { onIntent(JoinGameIntent.JoinGame(game)) }
                }
            } else if (state.hasPermissions) {
                item {
                    Text(
                        text = stringResource(R.string.join_game_empty),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
private fun JoinGameCard(modifier: Modifier = Modifier, game: Game, onGameClick: (Game) -> Unit) {
    Surface(
        modifier = modifier,
        tonalElevation = mediumSize,
        shape = MaterialTheme.shapes.medium,
        onClick = { onGameClick(game) }
    ) {
        Row(
            modifier = Modifier.padding(largeSize),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = game.title,
                style = MaterialTheme.typography.titleSmall
            )
            Row {
                Text(
                    text = stringResource(R.string.join_game_players, game.players, game.maxPlayers),
                    style = MaterialTheme.typography.bodyMedium
                )
                Icon(
                    modifier = Modifier.padding(start = mediumSize),
                    painter = AppIcons.ChevronRight,
                    contentDescription = null
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun JoinGameTopBar(onBack: () -> Unit) {
    TopAppBar(modifier = Modifier.fillMaxWidth(),
        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
        title = {
            Text(text = stringResource(R.string.join_game_title))
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(AppIcons.Back, contentDescription = null)
            }
        })
}

@Preview
@Composable
private fun JoinGameScreenPreview() {
    JoinGameScreen(
        state = JoinGameState().apply {
            games = mutableStateListOf(
                Game("1", "Game 1", "", "", 0, 0),
                Game("2", "Game 2", "", "", 0, 0),
                Game("3", "Game 3", "", "", 0, 0)
            )
        }
    ) {}
}