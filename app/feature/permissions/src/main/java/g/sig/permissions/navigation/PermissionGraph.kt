package g.sig.permissions.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import g.sig.permissions.PermissionViewModel
import g.sig.permissions.screens.PermissionScreen

@OptIn(ExperimentalPermissionsApi::class)
fun NavGraphBuilder.permissionGraph(
    navController: NavController
) {
    composable(PermissionRoute.path) {
        val viewModel = hiltViewModel<PermissionViewModel>()
        val permissionState = rememberMultiplePermissionsState(permissions = viewModel.permissions)

        PermissionScreen(permissionState) { navController.popBackStack() }
    }
}