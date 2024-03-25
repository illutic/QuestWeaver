package g.sig.onboarding.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import g.sig.onboarding.data.OnboardingViewModel
import g.sig.onboarding.screens.OnboardingScreen
import g.sig.onboarding.state.OnboardingEvent
import g.sig.onboarding.state.OnboardingIntent

@Composable
internal fun OnboardingRoute(
    onOnboardingComplete: () -> Unit,
    onNavigateToExplanation: () -> Unit
) {
    val viewModel = hiltViewModel<OnboardingViewModel>()
    val event by viewModel.events.collectAsState(OnboardingEvent.Idle)

    LaunchedEffect(event) {
        when (event) {
            OnboardingEvent.Idle -> viewModel.handleIntent(OnboardingIntent.Onboarding)
            OnboardingEvent.NavigateToExplanation -> onNavigateToExplanation()
            OnboardingEvent.OnboardingComplete -> onOnboardingComplete()
            else -> {}
        }
    }

    OnboardingScreen(onNavigateToExplanation)
}