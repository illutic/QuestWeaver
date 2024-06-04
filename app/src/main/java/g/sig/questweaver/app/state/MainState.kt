package g.sig.questweaver.app.state

import g.sig.questweaver.navigation.Route

sealed interface MainState {
    data object Loading : MainState
    data class Loaded(val startDestination: Route) : MainState
}
