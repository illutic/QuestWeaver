package g.sig.data.datasources.game

import g.sig.data.entities.Game
import kotlinx.coroutines.flow.StateFlow

interface GameSessionDataSource {
    val currentGameSession: StateFlow<Game>

    fun startGameSession(game: Game)
    fun updateGameSession(game: Game)
    fun endGameSession()
}