package g.sig.questweaver.home.state

import g.sig.questweaver.domain.entities.common.Game
import g.sig.questweaver.domain.entities.states.GameState

sealed interface HomeState {
    data object Idle : HomeState

    data object Loading : HomeState

    data class Loaded(
        val userName: String,
        val permissions: List<String>,
        val recentGames: Map<Game, GameState?>,
    ) : HomeState
}
