package g.sig.questweaver.data.repositories

import g.sig.questweaver.data.datasources.gamestate.GameStateDataSource
import g.sig.questweaver.data.mapper.toDomain
import g.sig.questweaver.data.mapper.toDto
import g.sig.questweaver.domain.entities.states.GameState
import g.sig.questweaver.domain.repositories.GameStateRepository

class GameStateRepositoryImpl(
    private val gameStateDataSource: GameStateDataSource,
) : GameStateRepository {
    override suspend fun getGameState(gameId: String?): GameState? =
        gameId?.let {
            gameStateDataSource.getGameState(it)?.toDomain()
        }

    override suspend fun removeGameState(gameId: String?) {
        gameId?.let { gameStateDataSource.deleteGameState(it) }
    }

    override suspend fun createGameState(game: GameState) {
        gameStateDataSource.createGameState(game.toDto())
    }

    override suspend fun updateGameState(game: GameState) {
        gameStateDataSource.updateGameState(game.toDto())
    }
}
