package g.sig.user.state

import androidx.annotation.StringRes
import g.sig.domain.entities.User

sealed interface UserState {
    data object Idle : UserState
    data object Loading : UserState

    sealed interface Loaded : UserState {
        val user: User
        val hasUser: Boolean get() = user.id.isNotBlank()

        data class Success(override val user: User) : Loaded
        data class Error(override val user: User, @StringRes val error: Int) : Loaded
    }
}

fun UserState.getError(): Int? = (this as? UserState.Loaded.Error)?.error