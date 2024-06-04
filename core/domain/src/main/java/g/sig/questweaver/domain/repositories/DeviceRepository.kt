package g.sig.questweaver.domain.repositories

import g.sig.questweaver.domain.entities.ConnectionState
import g.sig.questweaver.domain.entities.Device
import kotlinx.coroutines.flow.StateFlow

interface DeviceRepository {
    val devices: StateFlow<List<Device>>
    suspend fun addDevice(id: String, name: String)
    suspend fun removeDevice(id: String)
    suspend fun updateDevice(device: Device)
    suspend fun updateState(id: String?, state: ConnectionState)
    suspend fun updateState(state: ConnectionState)
}
