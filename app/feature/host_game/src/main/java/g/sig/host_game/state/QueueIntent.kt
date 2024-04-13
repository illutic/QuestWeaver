package g.sig.host_game.state

import g.sig.domain.entities.Device

sealed interface QueueIntent {
    data object Back : QueueIntent
    data object HostGame : QueueIntent
    data object CancelHostGame : QueueIntent
    data class AcceptConnection(val device: Device) : QueueIntent
    data class RejectConnection(val device: Device) : QueueIntent
    data object StartGame : QueueIntent
}