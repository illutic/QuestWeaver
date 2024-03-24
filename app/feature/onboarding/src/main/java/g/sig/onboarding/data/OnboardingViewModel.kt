package g.sig.onboarding.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import g.sig.domain.usecases.user.CreateUserUseCase
import g.sig.domain.usecases.user.ShouldShowOnBoardingUseCase
import g.sig.domain.usecases.user.ValidateUserUseCase
import g.sig.onboarding.R
import g.sig.onboarding.state.OnboardingEvent
import g.sig.onboarding.state.OnboardingIntent
import g.sig.onboarding.state.OnboardingState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val createUserUseCase: CreateUserUseCase,
    private val validateUserUseCase: ValidateUserUseCase,
    private val shouldShowOnBoardingUseCase: ShouldShowOnBoardingUseCase
) : ViewModel() {
    private val _events = Channel<OnboardingEvent>()
    internal val state = OnboardingState.NameState
    internal val events = _events.receiveAsFlow()

    private fun getErrorStringResource(validationState: ValidateUserUseCase.ValidationState): Int? {
        return when (validationState) {
            ValidateUserUseCase.ValidationState.EmptyName -> R.string.user_name_error
            ValidateUserUseCase.ValidationState.Valid -> null
        }
    }

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
                    val validationState = validateUserUseCase(intent.name)
                    val errorStringResource = getErrorStringResource(validationState)

                    state.error = errorStringResource
                    state.name = intent.name
                }

                is OnboardingIntent.CompleteOnboarding -> {
                    val validationState = validateUserUseCase(intent.name)
                    val errorStringResource = getErrorStringResource(validationState)

                    when (validationState) {
                        ValidateUserUseCase.ValidationState.Valid -> {
                            createUserUseCase(intent.name)
                            _events.send(OnboardingEvent.OnboardingComplete)
                        }

                        else -> {
                            state.error = errorStringResource
                        }
                    }
                }

                is OnboardingIntent.Back -> {
                    _events.send(OnboardingEvent.Back)
                }
            }
        }
    }
}