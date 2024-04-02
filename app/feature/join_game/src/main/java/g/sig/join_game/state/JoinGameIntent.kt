package g.sig.join_game.state

import g.sig.domain.entities.Game

sealed interface JoinGameIntent {
    data object Back : JoinGameIntent
    data object LoadGames : JoinGameIntent
    data object NavigateToPermissions : JoinGameIntent
    data class JoinGame(val game: Game) : JoinGameIntent
}