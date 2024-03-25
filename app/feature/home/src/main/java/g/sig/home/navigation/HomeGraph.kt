package g.sig.home.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import g.sig.home.data.HomeViewModel
import g.sig.home.screens.HomeScreen
import g.sig.home.state.HomeEvent

@OptIn(ExperimentalPermissionsApi::class)
fun NavGraphBuilder.homeGraph(
    onNavigateToProfile: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToHostGame: () -> Unit,
    onNavigateToJoinGame: () -> Unit,
    onNavigateToPermissions: () -> Unit,
    onNavigateToGame: (String) -> Unit,
) {
    composable(HomeRoute.path) {
        val viewModel = hiltViewModel<HomeViewModel>()
        val event by viewModel.events.collectAsState(HomeEvent.Idle)
        val permissionState = rememberMultiplePermissionsState(permissions = viewModel.state.permissions)

        LaunchedEffect(event) {
            when (val localEvent = event) {
                HomeEvent.NavigateToProfile -> onNavigateToProfile()
                HomeEvent.NavigateToSettings -> onNavigateToSettings()
                HomeEvent.NavigateToHost -> onNavigateToHostGame()
                HomeEvent.NavigateToJoin -> onNavigateToJoinGame()
                HomeEvent.NavigateToPermissions -> {
                    permissionState.launchMultiplePermissionRequest()
                    onNavigateToPermissions()
                }

                is HomeEvent.NavigateToGame -> onNavigateToGame(localEvent.gameId)
                else -> {}
            }
        }

        LaunchedEffect(permissionState.allPermissionsGranted) {
            viewModel.state.hasPermissions = permissionState.allPermissionsGranted
        }

        HomeScreen(
            homeState = viewModel.state,
            onIntent = viewModel::handleIntent
        )
    }
}