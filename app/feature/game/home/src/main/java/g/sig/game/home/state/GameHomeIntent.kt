package g.sig.game.home.state

sealed interface GameHomeIntent {
    data object Back : GameHomeIntent
}