package g.sig.questweaver.hostgame.state

import g.sig.questweaver.domain.entities.Device

sealed interface QueueIntent {
    data object Back : QueueIntent
    data object Load : QueueIntent
    data object CancelHostGame : QueueIntent
    data class AcceptConnection(val device: Device) : QueueIntent
    data class RejectConnection(val device: Device) : QueueIntent
    data object StartGame : QueueIntent
}
