package g.sig.questweaver.hostgame.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import g.sig.questweaver.domain.entities.common.Game
import g.sig.questweaver.domain.usecases.game.CreateGameUseCase
import g.sig.questweaver.domain.usecases.game.DeleteGameUseCase
import g.sig.questweaver.domain.usecases.host.VerifyDescriptionUseCase
import g.sig.questweaver.domain.usecases.host.VerifyGameNameUseCase
import g.sig.questweaver.domain.usecases.host.VerifyPlayerCountUseCase
import g.sig.questweaver.domain.usecases.permissions.GetNearbyPermissionUseCase
import g.sig.questweaver.domain.usecases.permissions.HasPermissionsUseCase
import g.sig.questweaver.hostgame.R
import g.sig.questweaver.hostgame.state.HostGameEvent
import g.sig.questweaver.hostgame.state.HostGameIntent
import g.sig.questweaver.hostgame.state.HostGameState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class HostGameViewModel
    @Inject
    constructor(
        private val createGame: CreateGameUseCase,
        private val deleteGame: DeleteGameUseCase,
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
                    HostGameIntent.Back -> sendEvent(HostGameEvent.Back)
                    HostGameIntent.LoadHostGame -> loadGame()
                    HostGameIntent.ShowConnectionDialog -> showConnectionDialog()
                    HostGameIntent.CancelHostGame -> cancelHosting()
                    HostGameIntent.NavigateToPermissions ->
                        sendEvent(HostGameEvent.NavigateToPermissions)

                    is HostGameIntent.SetDescription -> setDescription(intent)

                    is HostGameIntent.SetGameName -> setGameName(intent)

                    is HostGameIntent.SetPlayerCount -> setGamePlayerCount(intent)

                    is HostGameIntent.StartHosting -> hostGame()

                    HostGameIntent.NavigateToQueue -> sendEvent(HostGameEvent.NavigateToQueue)
                }
            }
        }

        private fun loadGame() {
            state.hasPermissions = hasPermissions(nearbyPermissions())
        }

        private fun setDescription(intent: HostGameIntent.SetDescription) {
            state.description = intent.description

            if (verifyDescription(state.description)) {
                state.descriptionError = null
            } else {
                state.descriptionError = R.string.invalid_description
            }
        }

        private fun setGameName(intent: HostGameIntent.SetGameName) {
            state.gameName = intent.gameName

            if (verifyGameName(state.gameName)) {
                state.gameNameError = null
            } else {
                state.gameNameError = R.string.invalid_title
            }
        }

        private fun setGamePlayerCount(intent: HostGameIntent.SetPlayerCount) {
            state.playerCount = intent.playerCount

            if (verifyPlayerCount(state.playerCount)) {
                state.playerCountError = null
            } else {
                state.playerCountError = R.string.invalid_max_players
            }
        }

        private suspend fun hostGame() {
            val verifiedGameTitle = verifyGameName(state.gameName)
            val verifiedDescription = verifyDescription(state.description)
            val verifiedPlayerCount = verifyPlayerCount(state.playerCount)
            val verifiedForm = verifiedGameTitle && verifiedDescription && verifiedPlayerCount

            if (!verifiedGameTitle) state.gameNameError = R.string.invalid_title
            if (!verifiedDescription) state.descriptionError = R.string.invalid_description
            if (!verifiedPlayerCount) state.playerCountError = R.string.invalid_max_players
            if (!state.hasPermissions) _events.emit(HostGameEvent.Error(R.string.permissions_error))

            if (verifiedForm && state.hasPermissions) {
                createGame(
                    Game(
                        gameId = UUID.randomUUID().toString(),
                        title = state.gameName,
                        description = state.description,
                        maxPlayers = state.playerCount!!,
                    ),
                )
                handleIntent(HostGameIntent.ShowConnectionDialog)
            }
        }

        private fun showConnectionDialog() {
            state.showConnectionDialog = true
        }

        private suspend fun cancelHosting() {
            deleteGame()
            _events.emit(HostGameEvent.CancelHostGame)
        }

        private suspend fun sendEvent(event: HostGameEvent) = _events.emit(event)
    }
