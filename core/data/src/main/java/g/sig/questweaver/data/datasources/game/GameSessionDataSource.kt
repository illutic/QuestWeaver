package g.sig.questweaver.data.datasources.game

import g.sig.questweaver.data.dto.GameStateDto
import kotlinx.coroutines.flow.StateFlow

interface GameSessionDataSource {
    val currentGameSessionDto: StateFlow<GameStateDto>

    suspend fun startGameSession(gameDto: GameStateDto)
    suspend fun updateGameSession(gameDto: GameStateDto)
    suspend fun endGameSession()
}
