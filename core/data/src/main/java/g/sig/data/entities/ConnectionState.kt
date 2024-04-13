package g.sig.data.entities

import g.sig.data.nearby.entities.AdvertiseState
import g.sig.data.nearby.entities.ConnectionState
import g.sig.data.nearby.entities.DiscoverState
import g.sig.domain.entities.ConnectionState as DomainConnectionState

fun ConnectionState.toDomain() = when (this) {
    ConnectionState.Loading,
    AdvertiseState.Advertising,
    DiscoverState.ConnectionRequested,
    DiscoverState.Discovering -> DomainConnectionState.Loading

    is DiscoverState.Discovered -> DomainConnectionState.Found(endpointId, name)
    is ConnectionState.Initiated -> DomainConnectionState.Connecting(endpointId, name)

    is ConnectionState.Connected -> DomainConnectionState.Connected(endpointId)

    is ConnectionState.Rejected -> DomainConnectionState.Error.RejectError(endpointId)
    is ConnectionState.Error -> DomainConnectionState.Error.GenericError(endpointId, message)
    is ConnectionState.Disconnected -> DomainConnectionState.Error.DisconnectionError(endpointId)

    DiscoverState.ConnectionRequestFailed -> DomainConnectionState.Error.ConnectionRequestError
    is ConnectionState.Failure -> DomainConnectionState.Error.FailureError(exception)
    is DiscoverState.Lost -> DomainConnectionState.Error.LostError(endpointId)
}