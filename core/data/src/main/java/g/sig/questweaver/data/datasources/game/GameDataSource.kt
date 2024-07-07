package g.sig.questweaver.data.datasources.game

import g.sig.questweaver.data.dto.GameDto
import kotlinx.coroutines.flow.StateFlow

interface GameDataSource {
    val currentGame: StateFlow<GameDto?>

    suspend fun createGame(gameDto: GameDto)

    suspend fun updateGame(gameDto: GameDto)

    suspend fun deleteGame(gameId: String)
}
