package g.sig.host_game.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import g.sig.common.ui.AppOutlinedTextField
import g.sig.common.ui.PermissionsAlert
import g.sig.host_game.R
import g.sig.host_game.state.HostGameIntent
import g.sig.host_game.state.HostGameState
import g.sig.ui.AppIcons
import g.sig.ui.components.Alert
import g.sig.ui.largeSize
import g.sig.ui.mediumSize

@Composable
internal fun HostGameScreen(
    snackbarHostState: SnackbarHostState,
    state: HostGameState,
    onIntent: (HostGameIntent) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = { HostGameTopBar { onIntent(HostGameIntent.Back) } }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = largeSize),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(largeSize)
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(mediumSize)
            ) {
                AsyncImage(
                    modifier = Modifier.size(HostGameSize.imageSize),
                    model = R.drawable.graphic_9,
                    contentDescription = ""
                )
                AppOutlinedTextField(
                    modifier = Modifier
                        .defaultMinSize(minWidth = HostGameSize.minTextSize)
                        .width(IntrinsicSize.Max),
                    value = state.gameName,
                    onValueChanged = { onIntent(HostGameIntent.SetGameName(it)) },
                    label = stringResource(R.string.game_title_label),
                    placeholder = stringResource(R.string.game_title_placeholder),
                    error = state.gameNameError?.let { stringResource(it) },
                    isLastField = false,
                )
                AppOutlinedTextField(
                    modifier = Modifier
                        .defaultMinSize(minWidth = HostGameSize.minTextSize)
                        .width(IntrinsicSize.Max),
                    value = state.description,
                    onValueChanged = { onIntent(HostGameIntent.SetDescription(it)) },
                    label = stringResource(R.string.game_description_label),
                    placeholder = stringResource(R.string.game_description_placeholder),
                    error = state.descriptionError?.let { stringResource(it) },
                    isLastField = false,
                )
                Row(
                    modifier = Modifier
                        .defaultMinSize(minWidth = HostGameSize.minTextSize)
                        .width(IntrinsicSize.Max),
                    horizontalArrangement = Arrangement.spacedBy(mediumSize),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.max_players_label),
                        style = MaterialTheme.typography.labelLarge
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    AppOutlinedTextField(
                        modifier = Modifier.widthIn(min = HostGameSize.numberTextSize),
                        style = MaterialTheme.typography.labelMedium.copy(textAlign = TextAlign.Center),
                        value = state.playerCount.toString(),
                        onValueChanged = { onIntent(HostGameIntent.SetPlayerCount(it.toIntOrNull() ?: 0)) },
                        placeholder = stringResource(R.string.max_players_placeholder),
                        error = state.playerCountError?.let { stringResource(it) },
                        isLastField = true,
                    )
                }
                Alert(
                    modifier = Modifier.width(IntrinsicSize.Max),
                    content = {
                        Text(
                            text = stringResource(R.string.host_game_alert),
                            style = MaterialTheme.typography.labelLarge
                        )
                    },
                    leadingContent = {
                        Icon(AppIcons.Info, contentDescription = null)
                    }
                )

                if (!state.hasPermissions) {
                    PermissionsAlert { onIntent(HostGameIntent.NavigateToPermissions) }
                }
            }

            Button(onClick = { onIntent(HostGameIntent.StartGame) }) {
                Text(text = stringResource(R.string.create_game_button))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HostGameTopBar(
    onBack: () -> Unit
) {
    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
        title = {
            Text(text = stringResource(R.string.top_bar_title))
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(AppIcons.Back, contentDescription = null)
            }
        })
}

@Preview
@Composable
private fun HostGameScreenPreview() {
    HostGameScreen(
        snackbarHostState = SnackbarHostState(),
        state = HostGameState(),
        onIntent = {}
    )
}