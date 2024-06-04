package g.sig.questweaver.user.state

sealed interface UserEvent {
    data object UserSaved : UserEvent
    data object Back : UserEvent
}
