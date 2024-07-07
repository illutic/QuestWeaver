package g.sig.questweaver.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import g.sig.questweaver.common.ui.components.CenteredProgressBar
import g.sig.questweaver.common.ui.components.ImageWithPlaceholder
import g.sig.questweaver.common.ui.layouts.ScreenScaffold
import g.sig.questweaver.settings.state.SettingsIntent
import g.sig.questweaver.settings.state.SettingsState
import g.sig.questweaver.ui.AppIcons
import g.sig.questweaver.ui.largeSize

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsTopBar(onBack: () -> Unit) {
    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
        title = {
            Text(
                text = stringResource(id = R.string.settings_title),
            )
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(AppIcons.Back, contentDescription = null)
            }
        },
    )
}

@Composable
private fun SettingsScreenContent(modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()

    ImageWithPlaceholder(
        modifier =
            Modifier
                .verticalScroll(scrollState)
                .padding(top = largeSize)
                .size(SettingsSize.imageSize),
        model = R.drawable.graphic_7,
        contentDescription = null,
    )

    Column(
        modifier =
            modifier
                .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.displayMedium,
            color = MaterialTheme.colorScheme.primary,
        )

        Text(
            text = stringResource(id = R.string.app_notice),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(vertical = largeSize),
        )
    }
}

@Composable
internal fun SettingsScreen(
    state: SettingsState,
    modifier: Modifier = Modifier,
    onIntent: (intent: SettingsIntent) -> Unit,
) {
    ScreenScaffold(
        topBar = { SettingsTopBar { onIntent(SettingsIntent.Back) } },
        navigation = {
            TextButton(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = largeSize),
                onClick = { onIntent(SettingsIntent.OpenPrivacyPolicy) },
            ) {
                Text(text = stringResource(id = R.string.privacy_policy))
            }
        },
    ) {
        when (state) {
            SettingsState.Loading -> CenteredProgressBar()

            SettingsState.Idle,
            is SettingsState.Loaded,
            -> {
                SettingsScreenContent(
                    modifier = modifier.padding(largeSize),
                )
            }
        }
    }
}
