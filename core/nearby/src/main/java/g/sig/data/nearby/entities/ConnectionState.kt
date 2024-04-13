package g.sig.data.nearby.entities

sealed interface ConnectionState {
    val endpointId: String? get() = null
    data object Loading : ConnectionState

    data class Disconnected(override val endpointId: String) : ConnectionState

    data class Failure(val exception: Exception) : ConnectionState

    data class Error(override val endpointId: String, val message: String? = null) : ConnectionState

    data class Initiated(override val endpointId: String, val name: String) : AdvertiseState

    data class Connected(override val endpointId: String) : AdvertiseState

    data class Rejected(override val endpointId: String) : AdvertiseState
}

sealed interface AdvertiseState : ConnectionState {
    data object Advertising : AdvertiseState
}

sealed interface DiscoverState : ConnectionState {
    data class Discovered(override val endpointId: String, val name: String) : DiscoverState

    data object Discovering : DiscoverState

    data object ConnectionRequested : DiscoverState

    data object ConnectionRequestFailed : DiscoverState

    data class Lost(override val endpointId: String) : DiscoverState
}
