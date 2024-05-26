package g.sig.join_game.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import g.sig.domain.entities.Game
import g.sig.domain.usecases.device.GetDevicesUseCase
import g.sig.domain.usecases.nearby.AcceptConnectionUseCase
import g.sig.domain.usecases.nearby.CancelDiscoveryUseCase
import g.sig.domain.usecases.nearby.DiscoverNearbyDevicesUseCase
import g.sig.domain.usecases.nearby.OnPayloadReceivedUseCase
import g.sig.domain.usecases.nearby.RejectConnectionUseCase
import g.sig.domain.usecases.nearby.RequestConnectionUseCase
import g.sig.domain.usecases.permissions.GetNearbyPermissionUseCase
import g.sig.domain.usecases.permissions.HasPermissionsUseCase
import g.sig.join_game.state.JoinGameEvent
import g.sig.join_game.state.JoinGameIntent
import g.sig.join_game.state.JoinGameState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JoinGameViewModel @Inject constructor(
    private val onPayloadReceived: OnPayloadReceivedUseCase,
    private val getNearbyPermissions: GetNearbyPermissionUseCase,
    private val hasPermissions: HasPermissionsUseCase,
    private val discoverDevices: DiscoverNearbyDevicesUseCase,
    private val cancelDiscovery: CancelDiscoveryUseCase,
    private val requestConnection: RequestConnectionUseCase,
    private val acceptConnection: AcceptConnectionUseCase,
    private val rejectConnection: RejectConnectionUseCase,
    private val getDevices: GetDevicesUseCase
) : ViewModel() {
    private val _events = Channel<JoinGameEvent>()
    val events = _events.receiveAsFlow()
    val state = JoinGameState()

    fun handleIntent(intent: JoinGameIntent) {
        when (intent) {
            JoinGameIntent.Back -> sendEvent(JoinGameEvent.Back)
            JoinGameIntent.NavigateToPermissions -> sendEvent(JoinGameEvent.NavigateToPermissions)
            JoinGameIntent.Load -> {
                state.hasPermissions = hasPermissions(*getNearbyPermissions().toTypedArray())
                cancelDiscovery()
                startDiscovery()
                collectDevices()
                collectPayloads()
            }

            is JoinGameIntent.JoinGame -> sendEvent(JoinGameEvent.JoinGame(intent.game))

            is JoinGameIntent.RequestConnection -> viewModelScope.launch { requestConnection(intent.device) }

            is JoinGameIntent.AcceptConnection -> viewModelScope.launch { acceptConnection(intent.device) }

            is JoinGameIntent.RejectConnection -> viewModelScope.launch { rejectConnection(intent.device) }
        }
    }

    private fun sendEvent(event: JoinGameEvent) = viewModelScope.launch { _events.send(event) }

    private fun startDiscovery() {
        state.discovering = true
        viewModelScope.launch { discoverDevices() }
    }

    private fun collectDevices() {
        viewModelScope.launch {
            getDevices().collect { devices ->
                state.devices.clear()
                state.devices.addAll(devices)
            }
        }
    }

    private fun collectPayloads() {
        viewModelScope.launch {
            onPayloadReceived { payload ->
                when (payload) {
                    is Game -> handleIntent(JoinGameIntent.JoinGame(payload))
                    else -> Unit
                }
            }
        }
    }
}