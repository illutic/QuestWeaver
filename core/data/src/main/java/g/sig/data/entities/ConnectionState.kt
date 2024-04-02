package g.sig.data.entities

import g.sig.data.nearby.entities.DiscoverState
import g.sig.data.nearby.entities.ConnectionState as NearbyConnectionState
import g.sig.domain.entities.ConnectionState as DomainConnectionState

enum class ConnectionState {
    Idle, Connecting, Connected, Disconnected;

    fun toDomain() = when (this) {
        Idle -> DomainConnectionState.Idle
        Connecting -> DomainConnectionState.Connecting
        Connected -> DomainConnectionState.Connected
        Disconnected -> DomainConnectionState.Disconnected
    }

    companion object {
        fun NearbyConnectionState.fromNearby() = when (this) {
            is DiscoverState.ConnectionRequested -> Connecting
            is NearbyConnectionState.Connected -> Connected
            is DiscoverState.Lost,
            is DiscoverState.ConnectionRequestFailed,
            is NearbyConnectionState.Rejected,
            is NearbyConnectionState.Error,
            is NearbyConnectionState.Failure,
            is NearbyConnectionState.Disconnected -> Disconnected

            else -> Idle
        }
    }
}