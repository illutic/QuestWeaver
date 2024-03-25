package g.sig.questweaver.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import g.sig.home.navigation.HomeRoute
import g.sig.home.navigation.homeGraph
import g.sig.navigation.Route
import g.sig.onboarding.navigation.OnboardingRoute
import g.sig.onboarding.navigation.onboardingGraph
import g.sig.permissions.navigation.PermissionRoute
import g.sig.permissions.navigation.permissionGraph

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    startDestination: Route?,
    navController: NavHostController
) {
    if (startDestination != null) {
        NavHost(
            modifier = modifier,
            navController = navController,
            startDestination = startDestination.path
        ) {
            onboardingGraph(navController) {
                navController.navigate(HomeRoute.path) {
                    popUpTo(OnboardingRoute.path) { inclusive = true }
                }
            }

            homeGraph(
                onNavigateToProfile = {},
                onNavigateToSettings = {},
                onNavigateToHostGame = {},
                onNavigateToJoinGame = {},
                onNavigateToPermissions = { navController.navigate(PermissionRoute.path) },
                onNavigateToGame = {}
            )

            permissionGraph(navController)
        }
    } else {
        Box(modifier, contentAlignment = Alignment.Center) { CircularProgressIndicator() }
    }
}