package g.sig.questweaver.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import g.sig.home.navigation.HomeRoute
import g.sig.home.navigation.homeGraph
import g.sig.onboarding.navigation.OnboardingRoute
import g.sig.onboarding.navigation.onboardingGraph

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = OnboardingRoute.path
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
            onNavigateToPermissions = {},
            onNavigateToGame = {}
        )
    }
}