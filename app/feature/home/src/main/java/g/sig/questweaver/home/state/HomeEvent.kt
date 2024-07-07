package g.sig.questweaver.home.state

import g.sig.questweaver.domain.entities.common.Game

internal sealed interface HomeEvent {
    data object Back : HomeEvent

    data object NavigateToOnboarding : HomeEvent

    data object NavigateToProfile : HomeEvent

    data object NavigateToSettings : HomeEvent

    data object NavigateToPermissions : HomeEvent

    data object NavigateToHost : HomeEvent

    data object NavigateToJoin : HomeEvent

    data class GameNotFound(
        val game: Game?,
    ) : HomeEvent

    data class NavigateToQueue(
        val gameId: String,
    ) : HomeEvent

    data class NavigateToGame(
        val gameId: String,
    ) : HomeEvent
}
