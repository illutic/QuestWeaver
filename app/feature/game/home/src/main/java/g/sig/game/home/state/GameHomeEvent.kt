package g.sig.game.home.state

sealed interface GameHomeEvent {
    data object Back : GameHomeEvent
}