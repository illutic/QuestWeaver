package g.sig.questweaver.home.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import g.sig.questweaver.domain.usecases.game.DeleteGameUseCase
import g.sig.questweaver.domain.usecases.game.ReconnectToGameUseCase
import g.sig.questweaver.domain.usecases.game.state.GetGameStateUseCase
import g.sig.questweaver.domain.usecases.home.GetHomeUseCase
import g.sig.questweaver.domain.usecases.user.HasUserUseCase
import g.sig.questweaver.home.state.HomeEvent
import g.sig.questweaver.home.state.HomeIntent
import g.sig.questweaver.home.state.HomeState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
    @Inject
    constructor(
        private val reconnectToGameUseCase: ReconnectToGameUseCase,
        private val removeGameSessionUseCase: DeleteGameUseCase,
        private val getHomeUseCase: GetHomeUseCase,
        private val getGameStateUseCase: GetGameStateUseCase,
        private val hasUser: HasUserUseCase,
    ) : ViewModel() {
        private val _events = Channel<HomeEvent>()
        private val _state = MutableStateFlow<HomeState>(HomeState.Idle)
        internal val state = _state.asStateFlow()
        internal val events = _events.receiveAsFlow()

        internal fun handleIntent(intent: HomeIntent) {
            viewModelScope.launch {
                when (intent) {
                    is HomeIntent.FetchHome -> {
                        _state.value = HomeState.Loading
                        val hasUser = hasUser()

                        if (!hasUser) {
                            _events.send(HomeEvent.NavigateToOnboarding)
                        } else {
                            val home = getHomeUseCase()
                            val recentGames =
                                home.recentGames.associateWith { game ->
                                    getGameStateUseCase(game.gameId)
                                }

                            _state.value =
                                HomeState.Loaded(
                                    userName = home.user.name,
                                    permissions = home.permissions.map { it.permission },
                                    recentGames = recentGames,
                                )
                        }
                    }

                    is HomeIntent.Back -> {
                        _events.send(HomeEvent.Back)
                    }

                    is HomeIntent.NavigateToGame -> {
                        reconnectToGameUseCase(
                            intent.gameId,
                            onReconnectionSuccess = {
                                _events.send(HomeEvent.NavigateToGame(intent.gameId))
                            },
                            onGameNotFound = {
                                val home = getHomeUseCase()
                                val game = home.recentGames.find { it.gameId == intent.gameId }
                                _events.send(HomeEvent.GameNotFound(game))
                            },
                        )
                    }

                    is HomeIntent.RemoveGame -> {
                        removeGameSessionUseCase(intent.gameId)
                        handleIntent(HomeIntent.FetchHome)
                    }

                    HomeIntent.NavigateToHost -> {
                        _events.send(HomeEvent.NavigateToHost)
                    }

                    HomeIntent.NavigateToJoin -> {
                        _events.send(HomeEvent.NavigateToJoin)
                    }

                    HomeIntent.NavigateToPermissions -> {
                        _events.send(HomeEvent.NavigateToPermissions)
                    }

                    HomeIntent.NavigateToProfile -> {
                        _events.send(HomeEvent.NavigateToProfile)
                    }

                    HomeIntent.NavigateToSettings -> {
                        _events.send(HomeEvent.NavigateToSettings)
                    }
                }
            }
        }
    }
