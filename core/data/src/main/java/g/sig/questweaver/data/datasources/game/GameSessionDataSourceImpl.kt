package g.sig.questweaver.data.datasources.game

import g.sig.questweaver.data.entities.common.GameDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class GameSessionDataSourceImpl : GameSessionDataSource {
    private val _currentGameSessionDto = MutableStateFlow(GameDto.Empty)
    override val currentGameSessionDto: StateFlow<GameDto> = _currentGameSessionDto.asStateFlow()

    override suspend fun startGameSession(gameDto: GameDto) {
        _currentGameSessionDto.value = gameDto
    }

    override suspend fun updateGameSession(gameDto: GameDto) {
        _currentGameSessionDto.value = gameDto
    }

    override suspend fun endGameSession() {
        _currentGameSessionDto.value = GameDto.Empty
    }
}
