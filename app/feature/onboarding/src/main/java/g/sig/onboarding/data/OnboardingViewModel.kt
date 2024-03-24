package g.sig.onboarding.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import g.sig.domain.usecases.user.CreateUserUseCase
import g.sig.domain.usecases.user.ShouldShowOnBoardingUseCase
import g.sig.onboarding.state.OnboardingEvent
import g.sig.onboarding.state.OnboardingIntent
import g.sig.onboarding.state.OnboardingState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val createUserUseCase: CreateUserUseCase,
    private val shouldShowOnBoardingUseCase: ShouldShowOnBoardingUseCase
) : ViewModel() {
    private val _events = Channel<OnboardingEvent>()
    private val _state = MutableStateFlow<OnboardingState>(OnboardingState.NameState(""))
    internal val state = _state.asStateFlow()
    internal val events = _events.receiveAsFlow()

    internal fun handleIntent(intent: OnboardingIntent) {
        viewModelScope.launch {
            when (intent) {
                is OnboardingIntent.Onboarding -> {
                    if (!shouldShowOnBoardingUseCase()) {
                        _events.send(OnboardingEvent.OnboardingComplete)
                    }
                }

                is OnboardingIntent.ContinueToExplanation -> {
                    _events.send(OnboardingEvent.NavigateToExplanation)
                }

                is OnboardingIntent.UpdateName -> {
                    _state.value = OnboardingState.NameState(intent.name)
                }

                is OnboardingIntent.CompleteOnboarding -> {
                    createUserUseCase(intent.name)
                    _events.send(OnboardingEvent.OnboardingComplete)
                }
            }
        }
    }
}