package g.sig.join_game.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import g.sig.domain.entities.ConnectionState
import g.sig.domain.entities.Device
import g.sig.domain.entities.NearbyAction
import g.sig.domain.usecases.nearby.AcceptConnectionUseCase
import g.sig.domain.usecases.nearby.DiscoverNearbyDevicesUseCase
import g.sig.domain.usecases.nearby.RejectConnectionUseCase
import g.sig.domain.usecases.nearby.RequestConnectionUseCase
import g.sig.domain.usecases.permissions.GetNearbyPermissionUseCase
import g.sig.domain.usecases.permissions.HasPermissionsUseCase
import g.sig.join_game.state.JoinGameEvent
import g.sig.join_game.state.JoinGameIntent
import g.sig.join_game.state.JoinGameState
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JoinGameViewModel @Inject constructor(
    private val getNearbyPermissions: GetNearbyPermissionUseCase,
    private val hasPermissions: HasPermissionsUseCase,
    private val getGames: DiscoverNearbyDevicesUseCase,
    private val requestConnection: RequestConnectionUseCase,
    private val acceptConnection: AcceptConnectionUseCase,
    private val rejectConnection: RejectConnectionUseCase
) : ViewModel() {
    private val _events = Channel<JoinGameEvent>()
    val events = _events.receiveAsFlow()
    val state = JoinGameState()

    private var internalIntentHandler: Job? = null
        set(value) {
            field?.cancel()
            field = value
        }

    private fun updateDeviceState(device: Device, connectionState: ConnectionState) {
        val localDevice = state.devices.find { it.id == device.id } ?: return
        val indexOfDevice = state.devices.indexOf(localDevice)
        state.devices[indexOfDevice] = localDevice.copy(connectionState = connectionState)
    }

    private fun addOrReplaceDevice(device: Device) {
        val deviceState = state.devices.find { it.id == device.id }
        if (deviceState != null) {
            val indexOfDevice = state.devices.indexOf(deviceState)
            state.devices[indexOfDevice] = device
        } else {
            state.devices.add(device)
        }
    }

    fun handleIntent(intent: JoinGameIntent) {
        internalIntentHandler =
            viewModelScope.launch {
                when (intent) {
                    JoinGameIntent.Back -> _events.send(JoinGameEvent.Back)
                    JoinGameIntent.LoadGames -> {
                        state.discovering = true
                        state.hasPermissions = hasPermissions(*getNearbyPermissions().toTypedArray())
                        getGames()
                            .onCompletion { state.discovering = false }
                            .collectLatest { nearbyAction ->
                                when (nearbyAction) {
                                    is NearbyAction.AddDevice -> addOrReplaceDevice(nearbyAction.device.copy(connectionState = ConnectionState.Idle))
                                    is NearbyAction.RemoveDevice -> state.devices.removeIf { it.id == nearbyAction.id }
                                }
                            }
                    }

                    JoinGameIntent.NavigateToPermissions -> _events.send(JoinGameEvent.NavigateToPermissions)
                    is JoinGameIntent.RequestConnection -> {
                        requestConnection(intent.device.id).collectLatest { updateDeviceState(intent.device, it) }
                    }

                    is JoinGameIntent.AcceptConnection -> {
                        acceptConnection(intent.device.id).collectLatest { updateDeviceState(intent.device, it) }
                    }

                    is JoinGameIntent.RejectConnection -> {
                        rejectConnection(intent.device.id).collectLatest { updateDeviceState(intent.device, it) }
                    }
                }
            }
    }
}