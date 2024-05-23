package g.sig.domain.repositories

import g.sig.domain.entities.ConnectionState
import g.sig.domain.entities.Device

interface DeviceRepository {
    suspend fun addDevice(id: String, name: String)
    suspend fun removeDevice(id: String)
    suspend fun getDevices(): List<Device>
    suspend fun updateDevice(device: Device)
    suspend fun updateState(id: String?, state: ConnectionState)
    suspend fun updateState(state: ConnectionState)
}