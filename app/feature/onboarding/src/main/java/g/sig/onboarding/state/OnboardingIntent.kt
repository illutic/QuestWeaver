package g.sig.onboarding.state

internal sealed interface OnboardingIntent {
    data object Onboarding : OnboardingIntent
    data object ContinueToExplanation : OnboardingIntent
    data class UpdateName(val name: String) : OnboardingIntent
    data class CompleteOnboarding(val name: String) : OnboardingIntent
}