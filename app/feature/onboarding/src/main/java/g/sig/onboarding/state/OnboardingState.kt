package g.sig.onboarding.state

internal sealed interface OnboardingState {
    data object Onboarding : OnboardingState
    data class NameUpdated(val name: String) : OnboardingState
}