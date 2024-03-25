package g.sig.home.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
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
        val permissionState = rememberMultiplePermissionsState(permissions = viewModel.state.permissions)

        LaunchedEffect(Unit) {
            viewModel.events.collect { event ->
                when (event) {
                    HomeEvent.NavigateToProfile -> onNavigateToProfile()
                    HomeEvent.NavigateToSettings -> onNavigateToSettings()
                    HomeEvent.NavigateToHost -> onNavigateToHostGame()
                    HomeEvent.NavigateToJoin -> onNavigateToJoinGame()
                    HomeEvent.NavigateToPermissions -> {
                        onNavigateToPermissions()
                    }

                    is HomeEvent.NavigateToGame -> onNavigateToGame(event.gameId)
                    else -> {}
                }
            }
        }

        LaunchedEffect(permissionState.allPermissionsGranted) {
            viewModel.state.hasPermissions = permissionState.allPermissionsGranted
        }

        Scaffold {
            HomeScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                homeState = viewModel.state,
                onIntent = viewModel::handleIntent
            )
        }
    }
}