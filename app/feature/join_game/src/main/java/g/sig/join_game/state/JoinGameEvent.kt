package g.sig.join_game.state

import g.sig.domain.entities.Game

sealed interface JoinGameEvent {
    data object Back : JoinGameEvent
    data object NavigateToPermissions : JoinGameEvent
    data class JoinGame(val game: Game) : JoinGameEvent
}