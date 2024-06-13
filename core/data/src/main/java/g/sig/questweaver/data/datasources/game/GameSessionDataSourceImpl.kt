package g.sig.questweaver.data.datasources.game

import g.sig.questweaver.data.entities.states.GameStateDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class GameSessionDataSourceImpl : GameSessionDataSource {
    private val _currentGameSessionDto = MutableStateFlow(GameStateDto.Empty)

    override val currentGameSessionDto: StateFlow<GameStateDto> =
        _currentGameSessionDto.asStateFlow()

    override suspend fun startGameSession(gameDto: GameStateDto) {
        _currentGameSessionDto.value = gameDto
    }

    override suspend fun updateGameSession(gameDto: GameStateDto) {
        _currentGameSessionDto.value = gameDto
    }

    override suspend fun endGameSession() {
        _currentGameSessionDto.value = GameStateDto.Empty
    }
}
