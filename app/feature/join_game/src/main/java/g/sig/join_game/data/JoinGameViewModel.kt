package g.sig.join_game.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import g.sig.domain.entities.NearbyAction
import g.sig.domain.usecases.nearby.GetNearbyGamesUseCase
import g.sig.domain.usecases.nearby.JoinGameUseCase
import g.sig.domain.usecases.permissions.GetNearbyPermissionUseCase
import g.sig.domain.usecases.permissions.HasPermissionsUseCase
import g.sig.join_game.state.JoinGameEvent
import g.sig.join_game.state.JoinGameIntent
import g.sig.join_game.state.JoinGameState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JoinGameViewModel @Inject constructor(
    private val getNearbyPermissions: GetNearbyPermissionUseCase,
    private val hasPermissions: HasPermissionsUseCase,
    private val getGames: GetNearbyGamesUseCase,
    private val joinGame: JoinGameUseCase
) : ViewModel() {
    private val _events = Channel<JoinGameEvent>()
    val events = _events.receiveAsFlow()
    val state = JoinGameState()

    fun handleIntent(intent: JoinGameIntent) {
        viewModelScope.launch {
            when (intent) {
                JoinGameIntent.Back -> _events.send(JoinGameEvent.Back)
                JoinGameIntent.LoadGames -> {
                    state.isLoading = true
                    state.hasPermissions = hasPermissions(*getNearbyPermissions().toTypedArray())
                    getGames()
                        .onCompletion { state.isLoading = false }
                        .collect { nearbyAction ->
                            when (nearbyAction) {
                                is NearbyAction.AddGame -> state.games.add(nearbyAction.game)
                                is NearbyAction.RemoveGame -> state.games.removeIf { it.id == nearbyAction.id }
                            }
                        }
                }

                JoinGameIntent.NavigateToPermissions -> _events.send(JoinGameEvent.NavigateToPermissions)
                is JoinGameIntent.JoinGame -> joinGame(intent.game)
            }
        }
    }
}