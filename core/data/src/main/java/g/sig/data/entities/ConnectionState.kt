package g.sig.data.entities

import g.sig.data.nearby.entities.AdvertiseState
import g.sig.data.nearby.entities.ConnectionState
import g.sig.data.nearby.entities.DiscoverState
import g.sig.domain.entities.ConnectionState as DomainConnectionState

fun ConnectionState.toDomain() = when (this) {
    is DiscoverState.Discovered,
    AdvertiseState.Advertising,
    DiscoverState.Discovering -> DomainConnectionState.Loading

    DiscoverState.ConnectionRequested,
    is ConnectionState.Initiated -> DomainConnectionState.Connecting

    is ConnectionState.Connected -> DomainConnectionState.Connected

    DiscoverState.ConnectionRequestFailed,
    is ConnectionState.Disconnected,
    is ConnectionState.Error,
    is ConnectionState.Rejected,
    is ConnectionState.Failure,
    is DiscoverState.Lost -> DomainConnectionState.Disconnected
}