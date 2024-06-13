package g.sig.questweaver.domain.repositories

import g.sig.questweaver.domain.entities.states.GameState

interface GameSessionRepository {
    suspend fun getGameSession(): GameState
    suspend fun startGameSession(game: GameState)
    suspend fun updateGameSession(game: GameState)
    suspend fun endGameSession()
}
