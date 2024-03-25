package g.sig.questweaver.state

import g.sig.navigation.Route

sealed interface MainState {
    data object Loading : MainState
    data class Loaded(val startDestination: Route) : MainState
}