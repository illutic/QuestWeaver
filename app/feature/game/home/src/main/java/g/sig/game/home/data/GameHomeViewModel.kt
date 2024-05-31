package g.sig.game.home.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import g.sig.game.home.state.GameHomeEvent
import g.sig.game.home.state.GameHomeIntent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameHomeViewModel @Inject constructor() : ViewModel() {
    private val _events = Channel<GameHomeEvent>()
    val events = _events.receiveAsFlow()

    fun handleIntent(intent: GameHomeIntent) {
        when (intent) {
            is GameHomeIntent.Back -> sendEvent(GameHomeEvent.Back)
        }
    }

    private fun sendEvent(event: GameHomeEvent) = viewModelScope.launch { _events.send(event) }
}