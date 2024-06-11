package g.sig.questweaver.data.entities.common

import g.sig.questweaver.data.entities.states.ConnectionStateDto
import g.sig.questweaver.data.entities.states.ConnectionStateDto.Companion.fromDomain
import g.sig.questweaver.domain.entities.common.Device
import kotlinx.serialization.Serializable

@Serializable
data class DeviceDto(
    val id: String,
    val name: String,
    val connectionState: ConnectionStateDto
) {
    fun toDomain() = Device(
        id = id,
        name = name,
        connectionState = connectionState.toDomain(),
    )

    companion object {
        fun Device.fromDomain() = DeviceDto(
            id = id,
            name = name,
            connectionState = connectionState.fromDomain(),
        )
    }
}
