package g.sig.questweaver.home.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
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
class HomeViewModel @Inject constructor(
    private val getHomeUseCase: GetHomeUseCase,
    private val hasUser: HasUserUseCase
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
                        _state.value = HomeState.Loaded(
                            userName = home.user.name,
                            permissions = home.permissions.map { it.permission },
                            recentGames = home.recentGames
                        )
                    }
                }

                is HomeIntent.Back -> {
                    _events.send(HomeEvent.Back)
                }

                is HomeIntent.NavigateToGame -> {
                    _events.send(HomeEvent.NavigateToGame(intent.gameId))
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
