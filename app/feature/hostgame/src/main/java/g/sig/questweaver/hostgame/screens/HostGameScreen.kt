package g.sig.questweaver.hostgame.screens

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import g.sig.questweaver.common.ui.components.Alert
import g.sig.questweaver.common.ui.components.AppOutlinedTextField
import g.sig.questweaver.common.ui.components.ImageWithPlaceholder
import g.sig.questweaver.common.ui.components.PermissionsAlert
import g.sig.questweaver.common.ui.layouts.ScreenScaffold
import g.sig.questweaver.hostgame.R
import g.sig.questweaver.hostgame.state.HostGameIntent
import g.sig.questweaver.hostgame.state.HostGameState
import g.sig.questweaver.navigation.SharedElementKeys
import g.sig.questweaver.ui.AppIcons
import g.sig.questweaver.ui.AppTheme
import g.sig.questweaver.ui.largeSize
import g.sig.questweaver.ui.mediumSize
import g.sig.questweaver.ui.sharedBounds
import g.sig.questweaver.ui.smallSize

@Composable
internal fun HostGameScreen(
    snackbarHostState: SnackbarHostState,
    state: HostGameState,
    animationScope: AnimatedContentScope,
    modifier: Modifier = Modifier,
    onIntent: (HostGameIntent) -> Unit
) {
    ScreenScaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = { HostGameTopBar { onIntent(HostGameIntent.Back) } },
        navigation = {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = largeSize)
                    .sharedBounds(SharedElementKeys.HOST_QUEUE_KEY, animationScope),
                onClick = { onIntent(HostGameIntent.StartHosting) }
            ) {
                Text(text = stringResource(R.string.create_game_button))
            }
        }
    ) {
        if (state.showConnectionDialog) {
            ConnectionDialog(
                onDismissRequest = { state.showConnectionDialog = false },
                onCancel = { onIntent(HostGameIntent.CancelHostGame) },
                onConfirm = { onIntent(HostGameIntent.NavigateToQueue) }
            )
        }

        HostGameContent(
            modifier = modifier,
            state = state,
            onIntent = onIntent
        )
    }
}

@Composable
private fun HostGameContent(
    state: HostGameState,
    modifier: Modifier = Modifier,
    onIntent: (HostGameIntent) -> Unit
) {
    val verticalScrollState = rememberScrollState()

    ImageWithPlaceholder(
        modifier = Modifier
            .padding(horizontal = largeSize)
            .verticalScroll(verticalScrollState)
            .size(HostGameSize.imageSize),
        model = R.drawable.graphic_9,
        contentDescription = ""
    )

    Column(
        modifier = modifier
            .verticalScroll(verticalScrollState)
            .padding(horizontal = largeSize)
            .width(IntrinsicSize.Max),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(smallSize)
    ) {
        NameAndDescriptionFields(state, onIntent)

        MaxPlayersInputField(state, onIntent)

        InformationAlert()

        if (!state.hasPermissions) {
            PermissionsAlert(modifier = Modifier.fillMaxWidth()) {
                onIntent(HostGameIntent.NavigateToPermissions)
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun NameAndDescriptionFields(state: HostGameState, onIntent: (HostGameIntent) -> Unit) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(smallSize)
    ) {
        AppOutlinedTextField(
            value = state.gameName,
            onValueChanged = { onIntent(HostGameIntent.SetGameName(it)) },
            label = stringResource(R.string.game_title_label),
            placeholder = stringResource(R.string.game_title_placeholder),
            error = state.gameNameError?.let { stringResource(it) },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )

        AppOutlinedTextField(
            value = state.description,
            onValueChanged = { onIntent(HostGameIntent.SetDescription(it)) },
            label = stringResource(R.string.game_description_label),
            placeholder = stringResource(R.string.game_description_placeholder),
            error = state.descriptionError?.let { stringResource(it) },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        )
    }
}

@Composable
private fun MaxPlayersInputField(state: HostGameState, onIntent: (HostGameIntent) -> Unit) {
    Row(
        modifier = Modifier
            .defaultMinSize(minWidth = HostGameSize.minTextSize),
        horizontalArrangement = Arrangement.spacedBy(mediumSize),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.max_players_label),
            style = MaterialTheme.typography.labelLarge
        )
        Spacer(modifier = Modifier.weight(1f))

        val keyboardController = LocalSoftwareKeyboardController.current
        val localFocusManager = LocalFocusManager.current

        AppOutlinedTextField(
            modifier = Modifier.widthIn(min = HostGameSize.numberTextSize),
            style = MaterialTheme.typography.labelMedium.copy(textAlign = TextAlign.Center),
            value = state.playerCount?.toString().orEmpty(),
            onValueChanged = {
                onIntent(HostGameIntent.SetPlayerCount(it.toIntOrNull()))
            },
            placeholder = stringResource(R.string.max_players_placeholder),
            error = state.playerCountError?.let { stringResource(it) },
            keyboardActions = KeyboardActions(onDone = {
                localFocusManager.clearFocus(true)
                keyboardController?.hide()
                onIntent(HostGameIntent.StartHosting)
            })
        )
    }
}

@Composable
private fun InformationAlert() {
    Alert(
        modifier = Modifier.fillMaxWidth(),
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HostGameTopBar(
    onBack: () -> Unit
) {
    AppTheme {
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
            }
        )
    }
}

@SuppressLint("UnusedContentLambdaTargetStateParameter")
@Preview
@Composable
private fun HostGameScreenPreview() {
    AppTheme {
        AnimatedContent(true, label = "queue_preview") {
            HostGameScreen(
                snackbarHostState = SnackbarHostState(),
                state = HostGameState(),
                animationScope = this,
                onIntent = {}
            )
        }
    }
}
