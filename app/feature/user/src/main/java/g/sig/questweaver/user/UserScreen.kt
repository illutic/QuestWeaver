package g.sig.questweaver.user

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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import g.sig.questweaver.common.ui.components.AdaptiveImage
import g.sig.questweaver.common.ui.components.Alert
import g.sig.questweaver.common.ui.components.AppOutlinedTextField
import g.sig.questweaver.common.ui.components.CenteredProgressBar
import g.sig.questweaver.common.ui.layouts.ScreenScaffold
import g.sig.questweaver.domain.entities.User
import g.sig.questweaver.ui.AppIcons
import g.sig.questweaver.ui.AppTheme
import g.sig.questweaver.ui.largeSize
import g.sig.questweaver.ui.mediumSize
import g.sig.questweaver.user.state.UserIntent
import g.sig.questweaver.user.state.UserState
import g.sig.questweaver.user.state.getError

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UserScreenTopBar(
    userState: UserState,
    onBack: () -> Unit
) {
    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
        title = {
            Text(
                text = when (userState) {
                    is UserState.Loaded.Success -> {
                        if (userState.user.id.isNotBlank()) {
                            stringResource(id = R.string.user_top_bar_update_title)
                        } else {
                            stringResource(id = R.string.user_top_bar_creation_title)
                        }
                    }

                    else -> stringResource(id = R.string.user_top_bar_update_title)
                },
            )
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(AppIcons.Back, contentDescription = null)
            }
        }
    )
}

@Composable
private fun UserScreenContent(
    modifier: Modifier = Modifier,
    name: String,
    error: Int?,
    onValueChanged: (String) -> Unit,
    onDone: () -> Unit
) {
    Column(
        modifier = modifier.padding(horizontal = largeSize),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AppOutlinedTextField(
            modifier = Modifier.defaultMinSize(minWidth = UserSize.minTextSize),
            value = name,
            error = error?.let { stringResource(it) },
            label = stringResource(R.string.user_name_label),
            placeholder = stringResource(R.string.user_name_placeholder),
            keyboardActions = KeyboardActions(onDone = { onDone() }),
            onValueChanged = onValueChanged
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
    }
}

@Composable
internal fun UserScreen(
    state: UserState,
    onIntent: (intent: UserIntent) -> Unit
) {
    var name by remember { mutableStateOf("") }
    val isUserPresent = (state as? UserState.Loaded.Success)?.user?.id?.isNotBlank() ?: false

    ScreenScaffold(
        topBar = { UserScreenTopBar(state) { onIntent(UserIntent.Back) } },
        navigation = {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(largeSize),
                onClick = { onIntent(UserIntent.SaveUser(name)) }
            ) {
                Text(text = stringResource(id = R.string.user_name_button))
            }
        },
        decoration = {
            AdaptiveImage(
                modifier = Modifier.size(UserSize.imageSize),
                model = if (isUserPresent) {
                    R.drawable.graphic_6
                } else {
                    R.drawable.graphic_2
                },
                contentDescription = null,
            )
        }
    ) {
        when (state) {
            is UserState.Loaded.Success -> {
                LaunchedEffect(state) {
                    name = state.user.name
                }
                UserScreenContent(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .fillMaxSize(),
                    name = name,
                    error = state.getError(),
                    onDone = { onIntent(UserIntent.SaveUser(name)) },
                    onValueChanged = { name = it }
                )
            }

            else -> CenteredProgressBar()
        }
    }
}

@Composable
@Preview
private fun UserScreenPreview() {
    AppTheme {
        UserScreen(
            state = UserState.Loaded.Success(
                User(
                    id = "1",
                    name = "Test"
                )
            ),
            onIntent = {}
        )
    }
}
