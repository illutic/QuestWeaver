package g.sig.data.datasources.game

import g.sig.data.entities.Game
import kotlinx.coroutines.flow.StateFlow

interface GameSessionDataSource {
    val currentGameSession: StateFlow<Game>

    suspend fun startGameSession(game: Game)
    suspend fun updateGameSession(game: Game)
    suspend fun endGameSession()
}