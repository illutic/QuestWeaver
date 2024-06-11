package g.sig.questweaver.data.entities.states

import g.sig.questweaver.domain.entities.states.ConnectionState
import kotlinx.serialization.Serializable

@Serializable
sealed interface ConnectionStateDto {

    fun toDomain(): ConnectionState

    data object IdleDto : ConnectionStateDto {
        override fun toDomain(): ConnectionState.Idle {
            return ConnectionState.Idle
        }
    }

    data object LoadingDto : ConnectionStateDto {
        override fun toDomain(): ConnectionState.Loading {
            return ConnectionState.Loading
        }
    }

    data class ConnectingDto(val endpointId: String, val name: String) : ConnectionStateDto {
        override fun toDomain(): ConnectionState.Connecting {
            return ConnectionState.Connecting(endpointId, name)
        }
    }

    data class ConnectedDto(val endpointId: String) : ConnectionStateDto {
        override fun toDomain(): ConnectionState.Connected {
            return ConnectionState.Connected(endpointId)
        }
    }

    data class FoundDto(val endpointId: String, val name: String) : ConnectionStateDto {
        override fun toDomain(): ConnectionState.Found {
            return ConnectionState.Found(endpointId, name)
        }
    }

    sealed class ErrorDto : Exception(), ConnectionStateDto {
        data class GenericError(val endpointId: String, override val message: String?) : ErrorDto()
        data class ConnectionRequestError(val throwable: Throwable? = null) : ErrorDto()

        data class DisconnectionError(val endpointId: String) : ErrorDto()
        data class RejectError(val endpointId: String) : ErrorDto()
        data class LostError(val endpointId: String) : ErrorDto()
        data class FailureError(val exception: Exception?) : ErrorDto()

        override fun toDomain(): ConnectionState.Error {
            return when (this) {
                is GenericError -> ConnectionState.Error.GenericError(endpointId, message)
                is ConnectionRequestError -> ConnectionState.Error.ConnectionRequestError(throwable)
                is DisconnectionError -> ConnectionState.Error.DisconnectionError(endpointId)
                is RejectError -> ConnectionState.Error.RejectError(endpointId)
                is LostError -> ConnectionState.Error.LostError(endpointId)
                is FailureError -> ConnectionState.Error.FailureError(exception)
            }
        }
    }

    companion object {
        fun ConnectionState.fromDomain(): ConnectionStateDto {
            return when (this) {
                is ConnectionState.Idle -> IdleDto
                is ConnectionState.Loading -> LoadingDto
                is ConnectionState.Connecting -> ConnectingDto(endpointId, name)
                is ConnectionState.Connected -> ConnectedDto(endpointId)
                is ConnectionState.Found -> FoundDto(endpointId, name)
                is ConnectionState.Error.GenericError -> ErrorDto.GenericError(endpointId, message)
                is ConnectionState.Error.RejectError -> ErrorDto.RejectError(endpointId)
                is ConnectionState.Error.LostError -> ErrorDto.LostError(endpointId)
                is ConnectionState.Error.FailureError -> ErrorDto.FailureError(exception)
                is ConnectionState.Error.ConnectionRequestError ->
                    ErrorDto.ConnectionRequestError(throwable)

                is ConnectionState.Error.DisconnectionError ->
                    ErrorDto.DisconnectionError(endpointId)
            }
        }
    }
}
