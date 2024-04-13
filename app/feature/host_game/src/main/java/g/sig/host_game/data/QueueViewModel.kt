package g.sig.host_game.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import g.sig.common.utils.update
import g.sig.domain.entities.Device.Companion.isTheSameAs
import g.sig.domain.usecases.host.GetGameSessionUseCase
import g.sig.domain.usecases.nearby.AcceptConnectionUseCase
import g.sig.domain.usecases.nearby.AdvertiseGameUseCase
import g.sig.domain.usecases.nearby.CancelAdvertisementGameUseCase
import g.sig.domain.usecases.nearby.RejectConnectionUseCase
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

    fun handleIntent(intent: QueueIntent) {
        viewModelScope.launch {
            when (intent) {

                QueueIntent.Back -> _events.emit(QueueEvent.Back)

                is QueueIntent.HostGame -> {
                    hostingJob =
                        launch {
                            advertiseGame(getGameSession().title)
                                .collectLatest {
                                    state.devicesToConnect.clear()
                                    state.devicesToConnect.addAll(it)
                                }
                        }
                }

                is QueueIntent.AcceptConnection -> connectionJob = launch {
                    acceptConnection(intent.device).collectLatest { connectionState ->
                        state.devicesToConnect.update(
                            { it isTheSameAs intent.device },
                            { it.copy(connectionState = connectionState) }
                        )
                    }
                }

                QueueIntent.CancelHostGame -> {
                    cancelAdvertisement()
                    _events.emit(QueueEvent.CancelHostGame)
                }

                QueueIntent.StartGame -> _events.emit(QueueEvent.GameCreated)

                is QueueIntent.RejectConnection -> connectionJob = launch {
                    rejectConnection(intent.device).collectLatest {
                        state.devicesToConnect.update(
                            { it isTheSameAs intent.device },
                            { it.copy(connectionState = it.connectionState) }
                        )
                    }
                }
            }
        }

    }
}