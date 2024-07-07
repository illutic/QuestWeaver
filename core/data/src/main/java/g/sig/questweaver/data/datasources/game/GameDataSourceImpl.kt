package g.sig.questweaver.data.datasources.game

import g.sig.questweaver.data.dto.GameDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class GameDataSourceImpl : GameDataSource {
    private val _currentGameSessionDto = MutableStateFlow<GameDto?>(null)

    override val currentGame: StateFlow<GameDto?> = _currentGameSessionDto.asStateFlow()

    override suspend fun createGame(gameDto: GameDto) {
        _currentGameSessionDto.value = gameDto
    }

    override suspend fun updateGame(gameDto: GameDto) {
        _currentGameSessionDto.value = gameDto
    }

    override suspend fun deleteGame(gameId: String) {
        _currentGameSessionDto.value = null
    }
}
