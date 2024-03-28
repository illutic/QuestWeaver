package g.sig.user.state

import g.sig.domain.entities.User

sealed interface UserIntent {
    data object Back : UserIntent
    data object LoadUser : UserIntent
    data class SaveUser(val user: User) : UserIntent
}