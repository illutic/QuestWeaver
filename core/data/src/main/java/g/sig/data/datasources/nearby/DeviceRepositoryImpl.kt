package g.sig.data.datasources.nearby

import g.sig.common.utils.addOrReplace
import g.sig.domain.entities.ConnectionState
import g.sig.domain.entities.Device
import g.sig.domain.repositories.DeviceRepository

class DeviceRepositoryImpl : DeviceRepository {
    private val devices = mutableListOf<Device>()

    override fun addDevice(id: String, name: String) {
        devices.addOrReplace(
            { it.name == name || it.id == id },
            Device(id, name, ConnectionState.Idle)
        )
    }

    override fun removeDevice(id: String) {
        devices.removeAll { it.id == id }
    }

    override fun getDevices(): List<Device> = devices

    override fun updateDevice(device: Device) {
        devices.addOrReplace({ it.id == device.id }, device)
    }

    override fun updateState(id: String?, state: ConnectionState) {
        if (id == null) return
        devices.addOrReplace(
            { it.id == id },
            devices
                .firstOrNull { it.id == id }
                ?.copy(connectionState = state) ?: return
        )
    }
}