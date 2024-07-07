package g.sig.questweaver.data.dto

import kotlinx.serialization.Serializable

@Serializable
sealed interface ConnectionStateDto : Dto {
    data object IdleDto : ConnectionStateDto

    data object LoadingDto : ConnectionStateDto

    data class ConnectingDto(
        val endpointId: String,
        val name: String,
    ) : ConnectionStateDto

    data class ConnectedDto(
        val endpointId: String,
    ) : ConnectionStateDto

    data class FoundDto(
        val endpointId: String,
        val name: String,
    ) : ConnectionStateDto

    sealed class ErrorDto :
        Exception(),
        ConnectionStateDto {
        data class GenericError(
            val endpointId: String,
            override val message: String?,
        ) : ErrorDto()

        data class ConnectionRequestError(
            val throwable: Throwable? = null,
        ) : ErrorDto()

        data class DisconnectionError(
            val endpointId: String,
        ) : ErrorDto()

        data class RejectError(
            val endpointId: String,
        ) : ErrorDto()

        data class LostError(
            val endpointId: String,
        ) : ErrorDto()

        data class FailureError(
            val exception: Exception?,
        ) : ErrorDto()
    }
}
