package g.sig.questweaver.settings

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import g.sig.questweaver.navigation.launchInBrowser
import g.sig.questweaver.settings.state.SettingsEvent
import kotlinx.coroutines.flow.collectLatest

fun NavGraphBuilder.settingsGraph(onBack: () -> Unit) {
    composable(SettingsRoute.path) {
        val viewModel = hiltViewModel<SettingsViewModel>()
        val state by viewModel.state.collectAsState()
        val context = LocalContext.current

        LaunchedEffect(Unit) {
            viewModel.events.collectLatest {
                when (it) {
                    is SettingsEvent.Back -> onBack()
                    is SettingsEvent.OpenPrivacyPolicy -> context.launchInBrowser(it.url)
                }
            }
        }

        SettingsScreen(
            state = state,
            animationScope = this,
            onIntent = viewModel::handleIntent,
        )
    }
}
