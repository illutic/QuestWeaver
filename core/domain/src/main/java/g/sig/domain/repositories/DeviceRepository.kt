package g.sig.domain.repositories

import g.sig.domain.entities.ConnectionState
import g.sig.domain.entities.Device
import kotlinx.coroutines.flow.StateFlow

interface DeviceRepository {
    val devices: StateFlow<List<Device>>
    suspend fun addDevice(id: String, name: String)
    suspend fun removeDevice(id: String)
    suspend fun updateDevice(device: Device)
    suspend fun updateState(id: String?, state: ConnectionState)
    suspend fun updateState(state: ConnectionState)
}