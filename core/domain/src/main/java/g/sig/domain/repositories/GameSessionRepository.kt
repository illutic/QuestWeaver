package g.sig.domain.repositories

import g.sig.domain.entities.Game

interface GameSessionRepository {
    fun startGameSession(game: Game)
    fun updateGameSession(game: Game)
    fun endGameSession()
}