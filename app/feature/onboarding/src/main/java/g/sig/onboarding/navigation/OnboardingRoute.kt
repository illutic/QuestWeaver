package g.sig.onboarding.navigation

import g.sig.navigation.Route

object OnboardingRoute : Route {
    override val path: String = "onboarding_path"
    internal const val ONBOARDING = "onboarding_path/onboarding"
    internal const val EXPLANATION_ROUTE = "onboarding_path/explanation"
    internal const val NAME_ROUTE = "onboarding_path/name"
}