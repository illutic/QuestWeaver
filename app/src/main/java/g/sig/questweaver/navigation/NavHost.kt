package g.sig.questweaver.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import g.sig.onboarding.navigation.OnboardingRoute
import g.sig.onboarding.navigation.onboardingGraph

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = OnboardingRoute.path) {
        onboardingGraph(navController) {}
    }
}