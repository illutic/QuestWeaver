package g.sig.questweaver.domain.repositories

import g.sig.questweaver.domain.entities.common.Device
import g.sig.questweaver.domain.entities.states.ConnectionState
import kotlinx.coroutines.flow.StateFlow

interface DeviceRepository {
    val devices: StateFlow<List<Device>>

    suspend fun getDevice(id: String): Device?

    suspend fun addDevice(
        id: String,
        name: String,
    )

    suspend fun removeDevice(id: String)

    suspend fun updateDevice(device: Device)

    suspend fun updateState(
        id: String?,
        state: ConnectionState,
    )

    suspend fun updateState(state: ConnectionState)
}
