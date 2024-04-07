package g.sig.domain.repositories

import g.sig.domain.entities.ConnectionState
import g.sig.domain.entities.Game
import g.sig.domain.entities.NearbyAction
import g.sig.domain.entities.User
import kotlinx.coroutines.flow.Flow

interface NearbyRepository {
    suspend fun getRecentGames(): List<Game>
    fun createGame(gameName: String, description: String, maxPlayers: Int): Flow<ConnectionState>
    fun findNearbyGames(user: User): Flow<NearbyAction>
    fun joinGame(user: User, game: Game): Flow<ConnectionState>
}