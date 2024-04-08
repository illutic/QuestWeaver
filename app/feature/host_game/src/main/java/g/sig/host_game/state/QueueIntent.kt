package g.sig.host_game.state

sealed interface QueueIntent {
    data object Back : QueueIntent
    data object HostGame : QueueIntent
    data object CancelHostGame : QueueIntent
    data class AcceptConnection(val endpointId: String) : QueueIntent
    data class RejectConnection(val endpointId: String) : QueueIntent
    data object StartGame : QueueIntent
}