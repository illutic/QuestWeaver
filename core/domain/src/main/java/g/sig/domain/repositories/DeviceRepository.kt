package g.sig.domain.repositories

import g.sig.domain.entities.ConnectionState
import g.sig.domain.entities.Device

interface DeviceRepository {
    fun addDevice(id: String, name: String)
    fun removeDevice(id: String)
    fun getDevices(): List<Device>
    fun updateDevice(device: Device)
    fun updateState(id: String?, state: ConnectionState)
    fun updateState(state: ConnectionState)
}