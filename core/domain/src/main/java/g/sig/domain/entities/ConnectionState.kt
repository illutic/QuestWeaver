package g.sig.domain.entities

sealed interface ConnectionState {
    data object Idle : ConnectionState
    data object Loading : ConnectionState
    data class Connecting(val endpointId: String, val name: String) : ConnectionState
    data class Connected(val endpointId: String) : ConnectionState
    data class Disconnected(val endpointId: String) : ConnectionState
    data object Failed : ConnectionState
}