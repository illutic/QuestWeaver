package g.sig.questweaver.hostgame.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import g.sig.questweaver.domain.usecases.device.GetDevicesUseCase
import g.sig.questweaver.domain.usecases.game.CreateGameUseCase
import g.sig.questweaver.domain.usecases.game.GetGameUseCase
import g.sig.questweaver.domain.usecases.nearby.AcceptConnectionUseCase
import g.sig.questweaver.domain.usecases.nearby.AdvertiseGameUseCase
import g.sig.questweaver.domain.usecases.nearby.BroadcastPayloadUseCase
import g.sig.questweaver.domain.usecases.nearby.CancelAdvertisementUseCase
import g.sig.questweaver.domain.usecases.nearby.RejectConnectionUseCase
import g.sig.questweaver.hostgame.state.QueueEvent
import g.sig.questweaver.hostgame.state.QueueIntent
import g.sig.questweaver.hostgame.state.QueueState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QueueViewModel
    @Inject
    constructor(
        private val advertiseGame: AdvertiseGameUseCase,
        private val cancelAdvertisement: CancelAdvertisementUseCase,
        private val getGameUseCase: GetGameUseCase,
        private val createGameUseCase: CreateGameUseCase,
        private val acceptConnection: AcceptConnectionUseCase,
        private val rejectConnection: RejectConnectionUseCase,
        private val broadcast: BroadcastPayloadUseCase,
        private val getDevices: GetDevicesUseCase,
    ) : ViewModel() {
        private val _events = MutableSharedFlow<QueueEvent>()
        val state = QueueState()
        val events = _events.asSharedFlow()

        fun handleIntent(intent: QueueIntent) {
            when (intent) {
                is QueueIntent.AcceptConnection -> viewModelScope.launch { acceptConnection(intent.device) }

                is QueueIntent.RejectConnection -> viewModelScope.launch { rejectConnection(intent.device) }

                QueueIntent.Back -> sendEvent(QueueEvent.Back)

                is QueueIntent.Load -> {
                    collectDevices()
                    viewModelScope.launch {
                        val game = intent.gameId?.let { getGameUseCase(it) }
                        if (game != null) {
                            createGameUseCase(game)
                        }

                        checkNotNull(game) { "Game session is null" }
                        advertiseGame(game.title)
                    }
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
                val game = getGameUseCase()
                checkNotNull(game) { "Game session is null" }

                broadcast(game)
                sendEvent(QueueEvent.GameCreated(game.gameId))
            }
        }
    }
