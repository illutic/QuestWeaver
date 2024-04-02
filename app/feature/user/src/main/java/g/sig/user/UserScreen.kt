package g.sig.user

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImage
import g.sig.ui.AppIcons
import g.sig.ui.LargeRoundedShape
import g.sig.ui.components.Alert
import g.sig.ui.components.CenteredProgressBar
import g.sig.ui.largeSize
import g.sig.ui.mediumSize
import g.sig.user.state.UserIntent
import g.sig.user.state.UserState
import g.sig.user.state.getError

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UserScreenTopBar(
    userState: UserState,
    onBack: () -> Unit
) {
    TopAppBar(modifier = Modifier.fillMaxWidth(),
        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
        title = {
            Text(
                text = when (userState) {
                    UserState.Idle,
                    UserState.Loading -> stringResource(id = R.string.user_top_bar_update_title)

                    is UserState.Loaded -> {
                        if (userState.hasUser) {
                            stringResource(id = R.string.user_top_bar_update_title)
                        } else {
                            stringResource(id = R.string.user_top_bar_creation_title)
                        }
                    }
                },
            )
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(AppIcons.Back, contentDescription = null)
            }
        })
}

@Composable
private fun OutlinedNameTextField(
    modifier: Modifier = Modifier,
    name: String,
    errorId: Int? = null,
    onSaveUser: () -> Unit,
    onValueChanged: (String) -> Unit
) {
    val keyboardActions = remember {
        KeyboardActions(onDone = {
            KeyboardActions.Default.onDone?.invoke(this)
            onSaveUser()
        })
    }

    OutlinedTextField(
        modifier = modifier,
        value = name,
        isError = errorId != null,
        onValueChange = onValueChanged,
        label = { Text(text = stringResource(id = R.string.user_name_label)) },
        placeholder = { Text(text = stringResource(id = R.string.user_name_placeholder)) },
        supportingText = {
            errorId?.let { error ->
                Text(
                    text = stringResource(id = error),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        keyboardActions = keyboardActions,
        singleLine = true,
        shape = LargeRoundedShape
    )
}

@Composable
private fun UserScreenContent(
    modifier: Modifier = Modifier,
    state: UserState.Loaded,
    onIntent: (intent: UserIntent) -> Unit
) {
    var name by remember { mutableStateOf(state.user.name) }
    val saveUserIntent = { onIntent(UserIntent.SaveUser(state.user.copy(name = name))) }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AsyncImage(
            modifier = Modifier.size(UserSize.imageSize),
            model = if (state.hasUser) {
                R.drawable.graphic_6
            } else {
                R.drawable.graphic_2
            },
            contentDescription = null,
        )

        OutlinedNameTextField(
            modifier = Modifier
                .defaultMinSize(minWidth = UserSize.minTextSize)
                .width(IntrinsicSize.Max),
            name = name,
            errorId = state.getError(),
            onSaveUser = saveUserIntent,
            onValueChanged = { name = it }
        )

        Alert(
            modifier = Modifier
                .width(IntrinsicSize.Max)
                .padding(vertical = mediumSize),
            content = {
                Text(
                    text = stringResource(id = R.string.user_alert_1),
                    style = MaterialTheme.typography.labelLarge
                )
            },
            leadingContent = {
                Icon(AppIcons.Info, contentDescription = null)
            }
        )

        Button(onClick = saveUserIntent) {
            Text(text = stringResource(id = R.string.user_name_button))
        }
    }
}

@Composable
internal fun UserScreen(
    state: UserState,
    onIntent: (intent: UserIntent) -> Unit
) {
    Scaffold(topBar = { UserScreenTopBar(state) { onIntent(UserIntent.Back) } }) { padding ->
        when (state) {
            UserState.Idle, UserState.Loading -> CenteredProgressBar()

            is UserState.Loaded -> {
                UserScreenContent(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .fillMaxSize()
                        .padding(padding)
                        .padding(largeSize),
                    state = state,
                    onIntent = onIntent
                )
            }
        }
    }
}