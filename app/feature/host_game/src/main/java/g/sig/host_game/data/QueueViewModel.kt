package g.sig.host_game.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import g.sig.domain.usecases.game.GetGameSessionUseCase
import g.sig.domain.usecases.nearby.AcceptConnectionUseCase
import g.sig.domain.usecases.nearby.AdvertiseGameUseCase
import g.sig.domain.usecases.nearby.BroadcastPayloadUseCase
import g.sig.domain.usecases.nearby.CancelAdvertisementGameUseCase
import g.sig.domain.usecases.nearby.GetDevicesUseCase
import g.sig.domain.usecases.nearby.RejectConnectionUseCase
import g.sig.host_game.state.QueueEvent
import g.sig.host_game.state.QueueIntent
import g.sig.host_game.state.QueueState
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
    private val rejectConnection: RejectConnectionUseCase,
    private val broadcast: BroadcastPayloadUseCase,
    private val getDevices: GetDevicesUseCase
) : ViewModel() {
    private val _events = MutableSharedFlow<QueueEvent>()
    val state = QueueState()
    val events = _events.asSharedFlow()

    fun handleIntent(intent: QueueIntent) {
        when (intent) {
            is QueueIntent.AcceptConnection -> viewModelScope.launch { acceptConnection(intent.device) }

            is QueueIntent.RejectConnection -> viewModelScope.launch { rejectConnection(intent.device) }

            QueueIntent.Back -> sendEvent(QueueEvent.Back)

            QueueIntent.Load -> {
                collectDevices()
                viewModelScope.launch { advertiseGame(getGameSession().title) }
            }

            QueueIntent.CancelHostGame -> cancelHosting()

            QueueIntent.StartGame -> startGame()
        }
    }

    private fun sendEvent(event: QueueEvent) {
        viewModelScope.launch { _events.emit(event) }
    }

    private fun collectDevices() {
        viewModelScope.launch {
            getDevices().collectLatest {
                state.devicesToConnect.clear()
                state.devicesToConnect.addAll(it)
            }
        }
    }

    private fun cancelHosting() {
        viewModelScope.launch { cancelAdvertisement() }
        sendEvent(QueueEvent.CancelHostGame)
    }

    private fun startGame() {
        viewModelScope.launch {
            broadcast(getGameSession())
            sendEvent(QueueEvent.GameCreated(getGameSession().gameId))
        }
    }
}
