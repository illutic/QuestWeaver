package g.sig.onboarding.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import g.sig.onboarding.data.OnboardingViewModel
import g.sig.onboarding.screens.OnboardingScreen
import g.sig.onboarding.state.OnboardingEvent

@Composable
internal fun OnboardingRoute(
    onOnboardingComplete: () -> Unit,
    onNavigateToExplanation: () -> Unit
) {
    val viewModel = hiltViewModel<OnboardingViewModel>()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                OnboardingEvent.NavigateToExplanation -> onNavigateToExplanation()
                OnboardingEvent.OnboardingComplete -> onOnboardingComplete()
                else -> {}
            }
        }
    }

    OnboardingScreen(onNavigateToExplanation)
}