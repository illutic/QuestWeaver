package g.sig.data.datasources.nearby

import g.sig.common.utils.addOrReplace
import g.sig.domain.entities.ConnectionState
import g.sig.domain.entities.Device
import g.sig.domain.repositories.DeviceRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DeviceRepositoryImpl(
    private val ioDispatcher: CoroutineDispatcher
) : DeviceRepository {
    private val devices = mutableListOf<Device>()

    override suspend fun addDevice(id: String, name: String) = withContext(ioDispatcher) {
        devices.addOrReplace(
            { it.name == name || it.id == id },
            Device(id, name, ConnectionState.Idle)
        )
    }

    override suspend fun removeDevice(id: String): Unit = withContext(ioDispatcher) {
        devices.removeAll { it.id == id }
    }

    override suspend fun getDevices(): List<Device> = withContext(ioDispatcher) {
        devices
    }

    override suspend fun updateDevice(device: Device): Unit = withContext(ioDispatcher) {
        devices.addOrReplace({ it.id == device.id }, device)
    }

    override suspend fun updateState(id: String?, state: ConnectionState) = withContext(ioDispatcher) {
        if (id == null) return@withContext

        devices.addOrReplace(
            { it.id == id },
            devices
                .firstOrNull { it.id == id }
                ?.copy(connectionState = state) ?: return@withContext
        )
    }

    override suspend fun updateState(state: ConnectionState) = withContext(ioDispatcher) {
        val endpointId = when (state) {
            is ConnectionState.Found -> state.endpointId
            is ConnectionState.Connecting -> state.endpointId
            is ConnectionState.Connected -> state.endpointId
            is ConnectionState.Error.DisconnectionError -> state.endpointId
            is ConnectionState.Error.RejectError -> state.endpointId
            is ConnectionState.Error.LostError -> state.endpointId
            else -> return@withContext
        }

        devices.addOrReplace(
            { it.id == endpointId },
            devices
                .firstOrNull { it.id == endpointId }
                ?.copy(connectionState = state) ?: return@withContext
        )
    }
}