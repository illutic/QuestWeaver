package g.sig.questweaver.domain.repositories

import g.sig.questweaver.domain.entities.states.GameState

interface GameStateRepository {
    suspend fun getGameState(gameId: String? = null): GameState?

    suspend fun removeGameState(gameId: String? = null)

    suspend fun createGameState(game: GameState)

    suspend fun updateGameState(game: GameState)
}
