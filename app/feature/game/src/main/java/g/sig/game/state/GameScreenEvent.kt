package g.sig.game.state

sealed interface GameScreenEvent {
    data object CloseGame : GameScreenEvent
    data object RequestCloseGame : GameScreenEvent
    data object DeviceDisconnected : GameScreenEvent
}