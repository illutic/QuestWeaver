package g.sig.questweaver.data.repositories

import g.sig.questweaver.data.datasources.nearby.NearbyDataSource
import g.sig.questweaver.data.datasources.recentgames.RecentGamesDataSource
import g.sig.questweaver.data.mapper.toDomain
import g.sig.questweaver.data.mapper.toDto
import g.sig.questweaver.domain.entities.common.Device
import g.sig.questweaver.domain.entities.common.Game
import g.sig.questweaver.domain.entities.common.User
import g.sig.questweaver.domain.entities.states.ConnectionState
import g.sig.questweaver.domain.repositories.NearbyRepository
import kotlinx.coroutines.flow.Flow

class NearbyRepositoryImpl(
    private val nearbyDataSource: NearbyDataSource,
    private val recentGamesDataStore: RecentGamesDataSource
) : NearbyRepository {
    override suspend fun getRecentGames(): List<Game> =
        recentGamesDataStore
            .getRecentGames()
            .map { it.toDomain() }

    override fun discoverNearbyDevices(user: User): Flow<ConnectionState> =
        nearbyDataSource.discover(user.toDto())

    override fun advertiseGame(game: String): Flow<ConnectionState> =
        nearbyDataSource.advertise(game)

    override fun cancelAdvertisement() = nearbyDataSource.cancelAdvertisement()

    override fun cancelDiscovery() = nearbyDataSource.cancelDiscovery()

    override fun requestConnection(user: User, device: Device): Flow<ConnectionState> =
        nearbyDataSource.requestConnection(user.toDto(), device.id)

    override fun acceptConnection(device: Device): Flow<ConnectionState> =
        nearbyDataSource.acceptConnection(device.id)

    override fun rejectConnection(device: Device): Flow<ConnectionState> =
        nearbyDataSource.rejectConnection(device.id)
}
