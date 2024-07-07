package g.sig.questweaver.data.nearby.entities

sealed interface ConnectionState {
    data object Loading : ConnectionState

    data class Disconnected(
        val endpointId: String,
    ) : ConnectionState

    data class Failure(
        val exception: Exception,
    ) : ConnectionState

    data class Error(
        val endpointId: String,
        val message: String? = null,
    ) : ConnectionState

    data class Initiated(
        val endpointId: String,
        val name: String,
    ) : AdvertiseState

    data class Connected(
        val endpointId: String,
    ) : AdvertiseState

    data class Rejected(
        val endpointId: String,
    ) : AdvertiseState
}

sealed interface AdvertiseState : ConnectionState {
    data object Advertising : AdvertiseState
}

sealed interface DiscoverState : ConnectionState {
    data class Discovered(
        val endpointId: String,
        val name: String,
    ) : DiscoverState

    data object Discovering : DiscoverState

    data object ConnectionRequested : DiscoverState

    data object ConnectionRequestFailed : DiscoverState

    data class Lost(
        val endpointId: String,
    ) : DiscoverState
}
