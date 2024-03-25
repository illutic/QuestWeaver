package g.sig.home.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import g.sig.domain.usecases.home.GetHomeUseCase
import g.sig.home.state.HomeEvent
import g.sig.home.state.HomeIntent
import g.sig.home.state.HomeState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getHomeUseCase: GetHomeUseCase
) : ViewModel() {
    private val _events = Channel<HomeEvent>()
    internal val state = HomeState()
    internal val events = _events.receiveAsFlow()

    internal fun handleIntent(intent: HomeIntent) {
        viewModelScope.launch {
            when (intent) {
                is HomeIntent.Back -> {
                    _events.send(HomeEvent.Back)
                }

                else -> {}
            }
        }
    }
}