package g.sig.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import g.sig.domain.usecases.user.GetUserUseCase
import g.sig.domain.usecases.user.UpdateUserNameUseCase
import g.sig.domain.usecases.user.ValidateUserNameUseCase
import g.sig.user.state.UserEvent
import g.sig.user.state.UserIntent
import g.sig.user.state.UserState
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
                    val user = intent.user
                    val validationState = validateUser(user.name)

                    when (validationState) {
                        is ValidateUserNameUseCase.ValidationState.Valid -> {
                            updateUserName(user.name)
                            _events.send(UserEvent.UserSaved)
                        }

                        is ValidateUserNameUseCase.ValidationState.EmptyName -> {
                            _state.value = UserState.Loaded.Error(user, R.string.user_name_error)
                        }
                    }
                }

                is UserIntent.Back -> _events.send(UserEvent.Back)
            }
        }
    }
}