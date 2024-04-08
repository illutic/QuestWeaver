package g.sig.host_game.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import g.sig.domain.entities.ConnectionState
import g.sig.domain.entities.Device
import g.sig.domain.usecases.host.GetGameSessionUseCase
import g.sig.domain.usecases.nearby.AcceptConnectionUseCase
import g.sig.domain.usecases.nearby.AdvertiseGameUseCase
import g.sig.domain.usecases.nearby.CancelAdvertisementGameUseCase
import g.sig.domain.usecases.nearby.RejectConnectionUseCase
import g.sig.host_game.R
import g.sig.host_game.state.QueueEvent
import g.sig.host_game.state.QueueIntent
import g.sig.host_game.state.QueueState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QueueViewModel @Inject constructor(
    private val advertiseGame: AdvertiseGameUseCase,
    private val cancelAdvertisement: CancelAdvertisementGameUseCase,
    private val getGameSession: GetGameSessionUseCase,
    private val acceptConnection: AcceptConnectionUseCase,
    private val rejectConnection: RejectConnectionUseCase
) : ViewModel() {
    private val _events = MutableSharedFlow<QueueEvent>()
    val state = QueueState()
    val events = _events.asSharedFlow()

    private var hostingJob: Job? = null
        set(value) {
            field?.cancel()
            field = value
        }
    private var connectionJob: Job? = null
        set(value) {
            field?.cancel()
            field = value
        }

    private fun addOrReplaceDevice(device: Device) {
        val index = state.devicesToConnect.indexOfFirst { it.id == device.id }
        if (index == -1) {
            state.devicesToConnect.add(device)
        } else {
            state.devicesToConnect[index] = device
        }
    }

    private fun updateDeviceToState(deviceId: String, connectionState: ConnectionState) {
        val index = state.devicesToConnect.indexOfFirst { it.id == deviceId }
        if (index != -1) {
            val device = state.devicesToConnect[index]
            state.devicesToConnect[index] = device.copy(connectionState = connectionState)
        }
    }

    fun handleIntent(intent: QueueIntent) {
        viewModelScope.launch {
            when (intent) {

                QueueIntent.Back -> _events.emit(QueueEvent.Back)

                is QueueIntent.HostGame -> hostingJob = launch {
                    advertiseGame(getGameSession().title).collectLatest {
                        when (it) {
                            ConnectionState.Idle -> _events.emit(QueueEvent.Error(R.string.advertising_error))
                            ConnectionState.Loading -> state.advertising = true
                            is ConnectionState.Connecting -> addOrReplaceDevice(Device(it.endpointId, it.name, ConnectionState.Idle))
                            is ConnectionState.Connected -> updateDeviceToState(it.endpointId, it)
                            is ConnectionState.Disconnected -> updateDeviceToState(it.endpointId, it)
                            ConnectionState.Failed -> _events.emit(QueueEvent.Error(R.string.advertising_error))
                        }
                    }
                }

                is QueueIntent.AcceptConnection -> connectionJob = launch {
                    acceptConnection(intent.endpointId).collectLatest { updateDeviceToState(intent.endpointId, it) }
                }

                QueueIntent.CancelHostGame -> {
                    cancelAdvertisement()
                    _events.emit(QueueEvent.CancelHostGame)
                }

                QueueIntent.StartGame -> _events.emit(QueueEvent.GameCreated)
                is QueueIntent.RejectConnection -> connectionJob = launch {
                    rejectConnection(intent.endpointId).collectLatest { updateDeviceToState(intent.endpointId, it) }
                }
            }
        }

    }
}