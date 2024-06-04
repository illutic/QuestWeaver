package g.sig.questweaver.user.state

import androidx.annotation.StringRes
import g.sig.questweaver.domain.entities.User

sealed interface UserState {
    data object Idle : UserState
    data object Loading : UserState

    sealed interface Loaded : UserState {
        data class Success(val user: User) : Loaded
        data class Error(val name: String, @StringRes val error: Int) : Loaded
    }
}

fun UserState.getError(): Int? = (this as? UserState.Loaded.Error)?.error
