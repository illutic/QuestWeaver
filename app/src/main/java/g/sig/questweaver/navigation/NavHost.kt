package g.sig.questweaver.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import g.sig.onboarding.navigation.OnboardingRoute
import g.sig.onboarding.navigation.onboardingGraph

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        navController = navController,
        startDestination = OnboardingRoute.path
    ) {
        onboardingGraph(navController) {}
    }
}