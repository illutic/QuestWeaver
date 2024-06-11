package g.sig.questweaver.data.datasources.game

import g.sig.questweaver.data.entities.common.GameDto
import kotlinx.coroutines.flow.StateFlow

interface GameSessionDataSource {
    val currentGameSessionDto: StateFlow<GameDto>

    suspend fun startGameSession(gameDto: GameDto)
    suspend fun updateGameSession(gameDto: GameDto)
    suspend fun endGameSession()
}
