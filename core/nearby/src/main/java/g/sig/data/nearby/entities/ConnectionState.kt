package g.sig.data.nearby.entities

import com.google.android.gms.nearby.connection.ConnectionInfo
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo

sealed interface ConnectionState {
    data object Loading : ConnectionState

    data class Disconnected(val endpointId: String) : ConnectionState

    data class Failure(val exception: Exception) : ConnectionState

    data class Error(val endpointId: String, val message: String? = null) : ConnectionState

    data class Initiated(val endpointId: String, val connectionInfo: ConnectionInfo?) : AdvertiseState

    data class Connected(val endpointId: String) : AdvertiseState

    data class Rejected(val endpointId: String) : AdvertiseState
}

sealed interface AdvertiseState : ConnectionState {
    data object Advertising : AdvertiseState
}

sealed interface DiscoverState : ConnectionState {
    data class Discovered(val endpointId: String, val info: DiscoveredEndpointInfo) : DiscoverState

    data object Discovering : DiscoverState

    data object ConnectionRequested : DiscoverState

    data object ConnectionRequestFailed : DiscoverState

    data class Lost(val endpointId: String) : DiscoverState
}
