package g.sig.questweaver

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import g.sig.domain.usecases.user.HasUserUseCase
import g.sig.home.navigation.HomeRoute
import g.sig.onboarding.navigation.OnboardingRoute
import g.sig.questweaver.state.MainState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val hasUserUseCase: HasUserUseCase,
) : ViewModel() {
    val state = flow {
        emit(MainState.Loading)
        emit(getLoadedState())
    }.stateIn(viewModelScope, SharingStarted.Eagerly, MainState.Loading)

    private suspend fun getLoadedState() = when {
        hasUserUseCase() -> MainState.Loaded(HomeRoute)
        else -> MainState.Loaded(OnboardingRoute)
    }
}