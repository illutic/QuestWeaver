package g.sig.permissions.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
    navController: NavController,
    onPermissionsGranted: () -> Unit
) {
    composable(PermissionRoute.path) {
        val viewModel = hiltViewModel<PermissionViewModel>()
        var userDeniedSomePermission by rememberSaveable { mutableStateOf(false) }
        val permissionState = rememberMultiplePermissionsState(permissions = viewModel.permissions) {
            userDeniedSomePermission = it.any { permissionState -> !permissionState.value }
            if (!userDeniedSomePermission) onPermissionsGranted()
        }

        PermissionScreen(permissionState, userDeniedSomePermission) { navController.popBackStack() }
    }
}