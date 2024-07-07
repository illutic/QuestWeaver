package g.sig.questweaver.data.datasources.gamestate

import g.sig.questweaver.data.dto.GameStateDto
import kotlinx.coroutines.flow.first

class GameStateDataSourceImpl(
    private val gameStatesDataStore: GameStatesDataStore,
) : GameStateDataSource {
    override suspend fun getGameState(gameId: String): GameStateDto? =
        gameStatesDataStore.data
            .first()
            .gameStatesDto
            .find { it.gameId == gameId }

    override suspend fun createGameState(gameStateDto: GameStateDto) {
        gameStatesDataStore.updateData { gameStatesDto ->
            gameStatesDto.copy(gameStatesDto = gameStatesDto.gameStatesDto + gameStateDto)
        }
    }

    override suspend fun updateGameState(gameStateDto: GameStateDto) {
        gameStatesDataStore.updateData { gameStatesDto ->
            gameStatesDto.copy(
                gameStatesDto =
                    gameStatesDto.gameStatesDto.map {
                        if (it.gameId == gameStateDto.gameId) gameStateDto else it
                    },
            )
        }
    }

    override suspend fun deleteGameState(gameId: String) {
        gameStatesDataStore.updateData { gameStatesDto ->
            gameStatesDto.copy(
                gameStatesDto =
                    gameStatesDto.gameStatesDto.filter {
                        it.gameId != gameId
                    },
            )
        }
    }
}
