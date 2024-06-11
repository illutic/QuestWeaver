package g.sig.questweaver.domain.repositories

import g.sig.questweaver.domain.entities.common.Game

interface GameSessionRepository {
    suspend fun getGameSession(): Game
    suspend fun startGameSession(game: Game)
    suspend fun updateGameSession(game: Game)
    suspend fun endGameSession()
}
