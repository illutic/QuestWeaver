package g.sig.domain.repositories

import g.sig.domain.entities.ConnectionState
import g.sig.domain.entities.Game
import g.sig.domain.entities.NearbyAction
import g.sig.domain.entities.User
import kotlinx.coroutines.flow.Flow

interface NearbyRepository {
    suspend fun getRecentGames(): List<Game>

    fun discoverNearbyDevices(user: User): Flow<NearbyAction>

    fun advertiseGame(game: String): Flow<ConnectionState>

    fun cancelAdvertisement()

    fun requestConnection(user: User, endpointId: String): Flow<ConnectionState>

    fun acceptConnection(endpointId: String): Flow<ConnectionState>

    fun rejectConnection(endpointId: String): Flow<ConnectionState>
}