package g.sig.onboarding.state

internal sealed interface OnboardingEvent {
    data object Idle : OnboardingEvent
    data object Back : OnboardingEvent
    data object NavigateToExplanation : OnboardingEvent
    data object OnboardingComplete : OnboardingEvent
}