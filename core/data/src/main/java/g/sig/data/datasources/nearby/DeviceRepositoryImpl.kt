package g.sig.data.datasources.nearby

import g.sig.domain.entities.ConnectionState
import g.sig.domain.entities.Device
import g.sig.domain.repositories.DeviceRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext

class DeviceRepositoryImpl(
    private val ioDispatcher: CoroutineDispatcher
) : DeviceRepository {
    private val _devices = MutableStateFlow<List<Device>>(listOf())
    override val devices: StateFlow<List<Device>> = _devices.asStateFlow()

    override suspend fun addDevice(id: String, name: String) = withContext(ioDispatcher) {
        if (_devices.value.none { it.id == id }) {
            _devices.value += Device(id, name, ConnectionState.Idle)
        }
    }

    override suspend fun removeDevice(id: String): Unit = withContext(ioDispatcher) {
        _devices.value = _devices.value.filter { it.id != id }
    }

    override suspend fun updateDevice(device: Device): Unit = withContext(ioDispatcher) {
        _devices.value = _devices.value.map {
            if (it.id == device.id) device else it
        }
    }

    override suspend fun updateState(id: String?, state: ConnectionState) = withContext(ioDispatcher) {
        _devices.value = _devices.value.map {
            if (it.id == id) it.copy(connectionState = state) else it
        }
    }

    override suspend fun updateState(state: ConnectionState) = withContext(ioDispatcher) {
        val endpointId = when (state) {
            is ConnectionState.Found -> state.endpointId
            is ConnectionState.Connecting -> state.endpointId
            is ConnectionState.Connected -> state.endpointId
            is ConnectionState.Error.DisconnectionError -> state.endpointId
            is ConnectionState.Error.RejectError -> state.endpointId
            is ConnectionState.Error.LostError -> state.endpointId
            is ConnectionState.Error.GenericError -> state.endpointId

            else -> return@withContext
        }
        updateState(endpointId, state)
    }
}