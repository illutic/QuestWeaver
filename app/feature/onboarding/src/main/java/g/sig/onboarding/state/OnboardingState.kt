package g.sig.onboarding.state

import androidx.annotation.StringRes
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

@Stable
internal sealed interface OnboardingState {

    @Stable
    data object NameState : OnboardingState {
        var name by mutableStateOf("")
        var error by mutableStateOf<@receiver:StringRes Int?>(null)
    }
}