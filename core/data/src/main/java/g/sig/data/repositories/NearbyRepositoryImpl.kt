package g.sig.data.repositories

import g.sig.data.datasources.nearby.NearbyDataSource
import g.sig.data.datasources.recentgames.RecentGamesDataSource
import g.sig.data.entities.User.Companion.fromDomain
import g.sig.data.entities.toDomain
import g.sig.domain.entities.Device
import g.sig.domain.entities.User
import g.sig.domain.repositories.NearbyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import g.sig.domain.entities.ConnectionState as DomainConnectionState
import g.sig.domain.entities.Game as DomainGame

class NearbyRepositoryImpl(
    private val nearbyDataSource: NearbyDataSource,
    private val recentGamesDataStore: RecentGamesDataSource
) : NearbyRepository {
    override suspend fun getRecentGames(): List<DomainGame> =
        recentGamesDataStore
            .getRecentGames()
            .map { it.toDomain() }

    override fun discoverNearbyDevices(user: User): Flow<DomainConnectionState> =
        nearbyDataSource
            .discover(user.fromDomain())
            .map { it.toDomain() }

    override fun advertiseGame(game: String): Flow<DomainConnectionState> =
        nearbyDataSource
            .advertise(game)
            .map { it.toDomain() }

    override fun cancelAdvertisement() = nearbyDataSource.cancelAdvertisement()

    override fun requestConnection(user: User, device: Device): Flow<DomainConnectionState> =
        nearbyDataSource
            .requestConnection(user.fromDomain(), device.id)
            .map { it.toDomain() }

    override fun acceptConnection(device: Device): Flow<DomainConnectionState> =
        nearbyDataSource
            .acceptConnection(device.id)
            .map { it.toDomain() }

    override fun rejectConnection(device: Device): Flow<DomainConnectionState> =
        nearbyDataSource
            .rejectConnection(device.id)
            .map { it.toDomain() }
}