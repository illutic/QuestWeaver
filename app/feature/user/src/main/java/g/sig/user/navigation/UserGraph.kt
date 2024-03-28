package g.sig.user.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import g.sig.user.UserScreen
import g.sig.user.UserViewModel
import g.sig.user.state.UserEvent
import g.sig.user.state.UserIntent

fun NavGraphBuilder.userGraph(
    onBack: () -> Unit,
    onUserSaved: () -> Unit
) {
    composable(UserRoute.path) {
        val viewModel = hiltViewModel<UserViewModel>()
        val state by viewModel.state.collectAsState()

        LaunchedEffect(Unit) {
            viewModel.handleIntent(UserIntent.LoadUser)

            viewModel.events.collect { event ->
                when (event) {
                    UserEvent.Back -> onBack()
                    UserEvent.UserSaved -> onUserSaved()
                }
            }
        }

        UserScreen(
            state = state,
            onIntent = viewModel::handleIntent
        )
    }
}