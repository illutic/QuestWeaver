package g.sig.user.state

sealed interface UserIntent {
    data object Back : UserIntent
    data object LoadUser : UserIntent
    data class SaveUser(val name: String) : UserIntent
}