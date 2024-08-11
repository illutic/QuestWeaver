package g.sig.questweaver.onboarding.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import g.sig.questweaver.onboarding.screens.ExplanationScreen
import g.sig.questweaver.onboarding.screens.WelcomeScreen

fun NavGraphBuilder.onboardingGraph(
    onNavigateToOnboarding: () -> Unit,
    onNavigateToUserCreation: () -> Unit,
) {
    composable(OnboardingRoute.WELCOME) {
        WelcomeScreen(
            animationScope = this,
            onNavigateNext = onNavigateToOnboarding,
        )
    }
    composable(OnboardingRoute.path) {
        ExplanationScreen(
            animationScope = this,
            onNavigateToUserCreation = onNavigateToUserCreation,
        )
    }
}
