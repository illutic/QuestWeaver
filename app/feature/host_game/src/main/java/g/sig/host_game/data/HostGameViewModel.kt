package g.sig.host_game.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import g.sig.domain.usecases.host.CreateGameSessionUseCase
import g.sig.domain.usecases.host.DeleteGameSessionUseCase
import g.sig.domain.usecases.host.VerifyDescriptionUseCase
import g.sig.domain.usecases.host.VerifyGameNameUseCase
import g.sig.domain.usecases.host.VerifyPlayerCountUseCase
import g.sig.domain.usecases.permissions.GetNearbyPermissionUseCase
import g.sig.domain.usecases.permissions.HasPermissionsUseCase
import g.sig.host_game.R
import g.sig.host_game.state.HostGameEvent
import g.sig.host_game.state.HostGameIntent
import g.sig.host_game.state.HostGameState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class HostGameViewModel @Inject constructor(
    private val createGame: CreateGameSessionUseCase,
    private val deleteGame: DeleteGameSessionUseCase,
    private val verifyDescription: VerifyDescriptionUseCase,
    private val verifyGameName: VerifyGameNameUseCase,
    private val verifyPlayerCount: VerifyPlayerCountUseCase,
    private val hasPermissions: HasPermissionsUseCase,
    private val nearbyPermissions: GetNearbyPermissionUseCase,
) : ViewModel() {
    private val _events = MutableSharedFlow<HostGameEvent>()
    val events = _events.asSharedFlow()
    val state = HostGameState()

    fun handleIntent(intent: HostGameIntent) {
        viewModelScope.launch {
            when (intent) {
                HostGameIntent.Back -> _events.emit(HostGameEvent.Back)
                HostGameIntent.NavigateToPermissions -> _events.emit(HostGameEvent.NavigateToPermissions)
                HostGameIntent.LoadHostGame -> state.hasPermissions = hasPermissions(*nearbyPermissions().toTypedArray())
                is HostGameIntent.SetDescription -> {
                    state.description = intent.description

                    if (verifyDescription(state.description)) {
                        state.descriptionError = null
                    } else {
                        state.descriptionError = R.string.invalid_description
                    }
                }

                is HostGameIntent.SetGameName -> {
                    state.gameName = intent.gameName

                    if (verifyGameName(state.gameName)) {
                        state.gameNameError = null
                    } else {
                        state.gameNameError = R.string.invalid_title
                    }
                }

                is HostGameIntent.SetPlayerCount -> {
                    state.playerCount = intent.playerCount

                    if (verifyPlayerCount(state.playerCount)) {
                        state.playerCountError = null
                    } else {
                        state.playerCountError = R.string.invalid_max_players
                    }
                }

                is HostGameIntent.StartHosting -> {
                    val verifiedGameTitle = verifyGameName(state.gameName)
                    val verifiedDescription = verifyDescription(state.description)
                    val verifiedPlayerCount = verifyPlayerCount(state.playerCount)

                    if (!verifiedGameTitle) state.gameNameError = R.string.invalid_title
                    if (!verifiedDescription) state.descriptionError = R.string.invalid_description
                    if (!verifiedPlayerCount) state.playerCountError = R.string.invalid_max_players
                    if (!state.hasPermissions) _events.emit(HostGameEvent.Error(R.string.permissions_error))

                    if (verifiedGameTitle && verifiedDescription && verifiedPlayerCount && state.hasPermissions) {
                        createGame(UUID.randomUUID().toString(), state.gameName, state.description, state.playerCount)
                        handleIntent(HostGameIntent.ShowConnectionDialog)
                    }
                }

                HostGameIntent.ShowConnectionDialog -> state.showConnectionDialog = true
                HostGameIntent.CancelHostGame -> {
                    deleteGame()
                    _events.emit(HostGameEvent.CancelHostGame)
                }

                HostGameIntent.NavigateToQueue -> _events.emit(HostGameEvent.NavigateToQueue)
            }
        }
    }
}