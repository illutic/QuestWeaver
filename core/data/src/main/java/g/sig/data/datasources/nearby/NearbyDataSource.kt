package g.sig.data.datasources.nearby

import g.sig.data.entities.Game
import g.sig.data.entities.User
import g.sig.data.nearby.entities.ConnectionState
import kotlinx.coroutines.flow.Flow

interface NearbyDataSource {
    fun findNearbyDevices(user: User): Flow<ConnectionState>
    fun joinGame(user: User, game: Game): Flow<ConnectionState>
    fun hostGame(game: Game): Flow<ConnectionState>
}