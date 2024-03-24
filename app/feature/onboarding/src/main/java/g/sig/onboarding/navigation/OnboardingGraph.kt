package g.sig.onboarding.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import g.sig.onboarding.screens.ExplanationScreen
import g.sig.onboarding.screens.NameScreen

fun NavGraphBuilder.onboardingGraph(
    navController: NavController,
    onOnboardingComplete: () -> Unit,
) {
    navigation(
        route = OnboardingRoute.path,
        startDestination = OnboardingRoute.ONBOARDING
    ) {
        composable(OnboardingRoute.ONBOARDING) {
            OnboardingRoute(
                onOnboardingComplete = onOnboardingComplete,
                onNavigateToExplanation = {
                    navController.navigate(OnboardingRoute.EXPLANATION_ROUTE) {
                        popUpTo(OnboardingRoute.path) { inclusive = true }
                    }
                }
            )
        }
        composable(OnboardingRoute.EXPLANATION_ROUTE) {
            ExplanationScreen(
                onNavigateToUserCreation = {
                    navController.navigate(OnboardingRoute.NAME_ROUTE)
                }
            )
        }
        composable(OnboardingRoute.NAME_ROUTE) {
            NameScreen(
                onUserCreated = {
                    onOnboardingComplete()
                }
            )
        }
    }
}