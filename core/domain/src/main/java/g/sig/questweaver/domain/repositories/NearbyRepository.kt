package g.sig.questweaver.domain.repositories

import g.sig.questweaver.domain.entities.ConnectionState
import g.sig.questweaver.domain.entities.Device
import g.sig.questweaver.domain.entities.Game
import g.sig.questweaver.domain.entities.User
import kotlinx.coroutines.flow.Flow

interface NearbyRepository {
    suspend fun getRecentGames(): List<Game>

    fun discoverNearbyDevices(user: User): Flow<ConnectionState>

    fun cancelDiscovery()

    fun advertiseGame(game: String): Flow<ConnectionState>

    fun cancelAdvertisement()

    fun requestConnection(user: User, device: Device): Flow<ConnectionState>

    fun acceptConnection(device: Device): Flow<ConnectionState>

    fun rejectConnection(device: Device): Flow<ConnectionState>
}
