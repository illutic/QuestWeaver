package g.sig.questweaver.data.datasources.gamestate

import g.sig.questweaver.data.dto.GameStateDto

interface GameStateDataSource {
    suspend fun getGameState(gameId: String): GameStateDto?

    suspend fun createGameState(gameStateDto: GameStateDto)

    suspend fun updateGameState(gameStateDto: GameStateDto)

    suspend fun deleteGameState(gameId: String)
}
