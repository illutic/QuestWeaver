package g.sig.join_game.state

import g.sig.domain.entities.Device

sealed interface JoinGameIntent {
    data object Back : JoinGameIntent
    data object LoadGames : JoinGameIntent
    data object NavigateToPermissions : JoinGameIntent
    data class RequestConnection(val device: Device) : JoinGameIntent
    data class AcceptConnection(val device: Device) : JoinGameIntent
    data class RejectConnection(val device: Device) : JoinGameIntent
}