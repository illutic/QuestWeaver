package g.sig.questweaver.onboarding.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import g.sig.questweaver.onboarding.screens.ExplanationScreen

fun NavGraphBuilder.onboardingGraph(
    onNavigateToUserCreation: () -> Unit,
) {
    composable(OnboardingRoute.path) {
        ExplanationScreen(onNavigateToUserCreation = onNavigateToUserCreation)
    }
}
