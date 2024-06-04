package g.sig.questweaver.game.home.state

sealed interface GameHomeIntent {
    data object Back : GameHomeIntent
}
