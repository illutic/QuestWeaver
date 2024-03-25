package g.sig.onboarding.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import g.sig.onboarding.R
import g.sig.onboarding.data.OnboardingViewModel
import g.sig.onboarding.state.NameState
import g.sig.onboarding.state.OnboardingEvent
import g.sig.onboarding.state.OnboardingIntent
import g.sig.ui.AppIcons
import g.sig.ui.LargeRoundedShape
import g.sig.ui.components.Alert
import g.sig.ui.largeSize
import g.sig.ui.mediumSize

@Composable
internal fun NameRoute(
    onBack: () -> Unit,
    onUserCreated: () -> Unit
) {
    val viewModel = hiltViewModel<OnboardingViewModel>()
    val event by viewModel.events.collectAsState(OnboardingEvent.Idle)

    LaunchedEffect(event) {
        when (event) {
            OnboardingEvent.OnboardingComplete -> onUserCreated()
            OnboardingEvent.Back -> onBack()
            else -> {}
        }
    }

    NameScreen(
        viewModel.state,
        viewModel::handleIntent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NameScreenTopBar(
    onBack: () -> Unit
) {
    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
        title = { Text(text = stringResource(id = R.string.user_top_bar_title)) },
        navigationIcon = {
            IconButton(onClick = { onBack() }) {
                Icon(AppIcons.Back, contentDescription = null)
            }
        }
    )
}

@Composable
private fun NameScreen(
    state: NameState,
    onIntent: (onBoardingIntent: OnboardingIntent) -> Unit
) {
    Scaffold(topBar = { NameScreenTopBar { onIntent(OnboardingIntent.Back) } }) { padding ->
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .padding(padding)
                .padding(largeSize),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AsyncImage(
                modifier = Modifier.size(OnboardingSize.imageSize),
                model = R.drawable.graphic_2,
                contentDescription = null,
            )

            OutlinedNameTextField(state = state, onIntent = onIntent)

            Alert(
                modifier = Modifier
                    .fillMaxWidth()
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

            Button(
                onClick = { onIntent(OnboardingIntent.CompleteOnboarding(state.name)) }
            ) {
                Text(text = stringResource(id = R.string.user_name_button))
            }
        }
    }
}

@Composable
private fun OutlinedNameTextField(
    state: NameState,
    onIntent: (onBoardingIntent: OnboardingIntent) -> Unit
) {
    val keyboardActions = remember {
        KeyboardActions(onDone = {
            KeyboardActions.Default.onDone?.invoke(this)
            onIntent(OnboardingIntent.CompleteOnboarding(state.name))
        })
    }

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = state.name,
        isError = state.error != null,
        onValueChange = {
            onIntent(OnboardingIntent.UpdateName(it))
        },
        label = { Text(text = stringResource(id = R.string.user_name_label)) },
        placeholder = { Text(text = stringResource(id = R.string.user_name_placeholder)) },
        supportingText = {
            state.error?.let { error ->
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