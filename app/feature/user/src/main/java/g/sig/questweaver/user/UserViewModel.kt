package g.sig.questweaver.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import g.sig.questweaver.domain.usecases.user.GetUserUseCase
import g.sig.questweaver.domain.usecases.user.UpdateUserNameUseCase
import g.sig.questweaver.domain.usecases.user.ValidateUserNameUseCase
import g.sig.questweaver.user.state.UserEvent
import g.sig.questweaver.user.state.UserIntent
import g.sig.questweaver.user.state.UserState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val getUser: GetUserUseCase,
    private val updateUserName: UpdateUserNameUseCase,
    private val validateUser: ValidateUserNameUseCase
) : ViewModel() {
    private val _events = Channel<UserEvent>()
    private val _state = MutableStateFlow<UserState>(UserState.Idle)
    val state = _state.asStateFlow()
    val events = _events.receiveAsFlow()

    fun handleIntent(intent: UserIntent) {
        viewModelScope.launch {
            when (intent) {
                UserIntent.LoadUser -> {
                    _state.value = UserState.Loading
                    _state.value = UserState.Loaded.Success(getUser())
                }

                is UserIntent.SaveUser -> {
                    _state.value = UserState.Loading
                    val validationState = validateUser(intent.name)

                    when (validationState) {
                        is ValidateUserNameUseCase.ValidationState.Valid -> {
                            updateUserName(intent.name)
                            _events.send(UserEvent.UserSaved)
                        }

                        is ValidateUserNameUseCase.ValidationState.EmptyName -> {
                            _state.value =
                                UserState.Loaded.Error(intent.name, R.string.user_name_error)
                        }
                    }
                }

                is UserIntent.Back -> _events.send(UserEvent.Back)
            }
        }
    }
}
