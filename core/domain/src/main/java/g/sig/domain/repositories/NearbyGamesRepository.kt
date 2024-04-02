package g.sig.domain.repositories

import g.sig.domain.entities.ConnectionState
import g.sig.domain.entities.Game
import g.sig.domain.entities.User
import kotlinx.coroutines.flow.Flow

interface NearbyGamesRepository {
    fun findNearbyGames(): Flow<Game>
    fun joinGame(user: User, game: Game): Flow<ConnectionState>
}