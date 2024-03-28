package g.sig.onboarding.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import g.sig.onboarding.screens.ExplanationScreen

fun NavGraphBuilder.onboardingGraph(
    onNavigateToUserCreation: () -> Unit,
) {
    composable(OnboardingRoute.path) {
        ExplanationScreen(onNavigateToUserCreation = onNavigateToUserCreation)
    }
}