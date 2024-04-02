package g.sig.data.datasources.nearby

import g.sig.data.entities.ConnectionState
import g.sig.data.entities.Game
import g.sig.data.entities.User
import kotlinx.coroutines.flow.Flow

interface NearbyGameDataSource {
    fun findNearbyGames(): Flow<Game>
    fun joinGame(user: User, game: Game): Flow<ConnectionState>
}