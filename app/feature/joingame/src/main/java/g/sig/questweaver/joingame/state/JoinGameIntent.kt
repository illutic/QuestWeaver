package g.sig.questweaver.joingame.state

import g.sig.questweaver.domain.entities.Device
import g.sig.questweaver.domain.entities.Game

sealed interface JoinGameIntent {
    data object Back : JoinGameIntent
    data object Load : JoinGameIntent
    data object NavigateToPermissions : JoinGameIntent
    data class RequestConnection(val device: Device) : JoinGameIntent
    data class AcceptConnection(val device: Device) : JoinGameIntent
    data class RejectConnection(val device: Device) : JoinGameIntent
    data class JoinGame(val game: Game) : JoinGameIntent
}
