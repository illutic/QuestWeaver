package g.sig.questweaver.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class DeviceDto(
    val id: String,
    val name: String,
    val connectionState: ConnectionStateDto,
) : Dto
