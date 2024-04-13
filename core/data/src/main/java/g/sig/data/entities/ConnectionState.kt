package g.sig.data.entities

import g.sig.data.nearby.entities.AdvertiseState
import g.sig.data.nearby.entities.ConnectionState
import g.sig.data.nearby.entities.DiscoverState
import g.sig.domain.entities.ConnectionState as DomainConnectionState

fun ConnectionState.toDomain() = when (this) {
    is DiscoverState.Discovered,
    ConnectionState.Loading,
    AdvertiseState.Advertising,
    DiscoverState.ConnectionRequested,
    DiscoverState.Discovering -> DomainConnectionState.Loading

    is ConnectionState.Initiated -> DomainConnectionState.Connecting(endpointId, name)

    is ConnectionState.Connected -> DomainConnectionState.Connected(endpointId)

    is ConnectionState.Disconnected -> DomainConnectionState.Disconnected(endpointId)

    DiscoverState.ConnectionRequestFailed,
    is ConnectionState.Error,
    is ConnectionState.Rejected,
    is ConnectionState.Failure,
    is DiscoverState.Lost -> DomainConnectionState.Failed
}