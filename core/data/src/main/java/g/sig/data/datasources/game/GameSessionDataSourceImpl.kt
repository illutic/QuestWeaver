package g.sig.data.datasources.game

import g.sig.data.entities.Game
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class GameSessionDataSourceImpl : GameSessionDataSource {
    private val _currentGameSession = MutableStateFlow(Game.Empty)
    override val currentGameSession: StateFlow<Game> = _currentGameSession.asStateFlow()

    override suspend fun startGameSession(game: Game) {
        _currentGameSession.value = game
    }

    override suspend fun updateGameSession(game: Game) {
        _currentGameSession.value = game
    }

    override suspend fun endGameSession() {
        _currentGameSession.value = Game.Empty
    }
}