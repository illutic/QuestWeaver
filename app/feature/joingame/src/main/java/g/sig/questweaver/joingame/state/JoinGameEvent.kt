package g.sig.questweaver.joingame.state

import g.sig.questweaver.domain.entities.common.Game

sealed interface JoinGameEvent {
    data object Back : JoinGameEvent

    data object NavigateToPermissions : JoinGameEvent

    data class JoinGame(
        val game: Game,
    ) : JoinGameEvent
}
