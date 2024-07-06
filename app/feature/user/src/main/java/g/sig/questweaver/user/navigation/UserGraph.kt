package g.sig.questweaver.user.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import g.sig.questweaver.navigation.SharedElementKeys
import g.sig.questweaver.ui.sharedBounds
import g.sig.questweaver.user.UserScreen
import g.sig.questweaver.user.UserViewModel
import g.sig.questweaver.user.state.UserEvent
import g.sig.questweaver.user.state.UserIntent
import kotlinx.coroutines.flow.collectLatest

fun NavGraphBuilder.userGraph(
    onBack: () -> Unit,
    onUserSaved: () -> Unit
) {
    composable(UserRoute.path) {
        val viewModel = hiltViewModel<UserViewModel>()
        val state by viewModel.state.collectAsState()

        LaunchedEffect(Unit) {
            viewModel.handleIntent(UserIntent.LoadUser)

            viewModel.events.collectLatest { event ->
                when (event) {
                    UserEvent.Back -> onBack()
                    UserEvent.UserSaved -> onUserSaved()
                }
            }
        }

        UserScreen(
            modifier = Modifier.sharedBounds(
                key = SharedElementKeys.PROFILE_KEY,
                animationScope = this
            ),
            state = state,
            onIntent = viewModel::handleIntent
        )
    }
}
