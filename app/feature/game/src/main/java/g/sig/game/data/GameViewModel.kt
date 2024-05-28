package g.sig.game.data

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import g.sig.domain.usecases.device.GetDevicesUseCase
import g.sig.game.ai.navigation.GameAiRoute
import g.sig.game.chat.navigation.GameChatRoute
import g.sig.game.home.R
import g.sig.game.home.navigation.GameHomeRoute
import g.sig.game.state.GameIntent
import g.sig.game.state.GameScreenEvent
import g.sig.navigation.DecoratedRoute
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val getDevices: GetDevicesUseCase
) : ViewModel() {
    private val _events = MutableSharedFlow<GameScreenEvent>()
    val events = _events.asSharedFlow()
    var selectedRoute: DecoratedRoute by mutableStateOf(GameHomeRoute(label = context.getString(R.string.game_home_label)))
    val gameRoutes = listOf(
        GameHomeRoute(label = context.getString(R.string.game_home_label)),
        GameChatRoute(label = context.getString(R.string.game_chat_label)),
        GameAiRoute(label = context.getString(R.string.game_ai_label)),
    )

    fun handleIntent(intent: GameIntent) {
        when (intent) {
            is GameIntent.Load -> listenForConnectedState()
            is GameIntent.RequestCloseGame -> sendEvent(GameScreenEvent.RequestCloseGame)
            is GameIntent.CloseGame -> sendEvent(GameScreenEvent.CloseGame)
            is GameIntent.SelectRoute -> gameRoutes.find { it == intent.route }?.let { selectedRoute = it }
        }
    }

    private fun listenForConnectedState() {
        viewModelScope.launch {
            getDevices().collect { devices ->
                if (devices.isEmpty()) {
                    sendEvent(GameScreenEvent.DeviceDisconnected)
                }
            }
        }
    }

    private fun sendEvent(event: GameScreenEvent) {
        viewModelScope.launch {
            _events.emit(event)
        }
    }
}