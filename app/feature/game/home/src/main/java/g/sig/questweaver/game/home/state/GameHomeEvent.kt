package g.sig.questweaver.game.home.state

sealed interface GameHomeEvent {
    data object Back : GameHomeEvent
}
