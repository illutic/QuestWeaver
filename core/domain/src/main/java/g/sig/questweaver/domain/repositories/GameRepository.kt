package g.sig.questweaver.domain.repositories

import g.sig.questweaver.domain.entities.common.Game

interface GameRepository {
    suspend fun getGame(gameId: String? = null): Game?

    suspend fun removeGame(gameId: String? = null)

    suspend fun createGame(game: Game)

    suspend fun updateGame(game: Game)
}
