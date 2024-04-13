package g.sig.domain.entities

sealed interface ConnectionState {
    data object Idle : ConnectionState
    data object Loading : ConnectionState
    data class Connecting(val endpointId: String, val name: String) : ConnectionState
    data class Connected(val endpointId: String) : ConnectionState
    data class Found(val endpointId: String, val name: String) : ConnectionState
    sealed class Error : Exception(), ConnectionState {
        data class GenericError(val endpointId: String, override val message: String?) : Error()
        data object ConnectionRequestError : Error() {
            private fun readResolve(): Any = ConnectionRequestError
        }

        data class DisconnectionError(val endpointId: String) : Error()
        data class RejectError(val endpointId: String) : Error()
        data class LostError(val endpointId: String) : Error()
        data class FailureError(val exception: Exception?) : Error()
    }
}