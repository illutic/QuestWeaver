package g.sig.questweaver.domain.repositories

import g.sig.questweaver.domain.entities.common.Device
import g.sig.questweaver.domain.entities.common.Game
import g.sig.questweaver.domain.entities.common.User
import g.sig.questweaver.domain.entities.states.ConnectionState
import kotlinx.coroutines.flow.Flow

interface NearbyRepository {
    suspend fun getRecentGames(): List<Game>

    fun discoverNearbyDevices(user: User): Flow<ConnectionState>

    fun cancelDiscovery()

    fun advertiseGame(game: String): Flow<ConnectionState>

    fun cancelAdvertisement()

    fun requestConnection(
        user: User,
        deviceId: String,
    ): Flow<ConnectionState>

    fun acceptConnection(device: Device): Flow<ConnectionState>

    fun rejectConnection(device: Device): Flow<ConnectionState>
}
