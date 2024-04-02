package g.sig.home.state

import g.sig.domain.entities.Game

sealed interface HomeState {
    data object Idle : HomeState
    data object Loading : HomeState
    data class Loaded(
        val userName: String,
        val permissions: List<String>,
        val recentGames: List<Game>
    ) : HomeState
}