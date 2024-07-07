package g.sig.questweaver.data.dto

import kotlinx.serialization.Serializable

@Serializable
sealed interface PayloadDataDto {
    val data: Dto

    @Serializable
    data class Broadcast(
        override val data: Dto,
    ) : PayloadDataDto

    @Serializable
    data class Unicast(
        override val data: Dto,
        val destination: String
    ) : PayloadDataDto
}

@Serializable
data class IncomingPayloadDto(
    val origin: String,
    val payloadData: PayloadDataDto
)
