package g.sig.data.repositories

import g.sig.data.datasources.nearby.NearbyDataSource
import g.sig.data.datasources.recentgames.RecentGamesDataSource
import g.sig.data.entities.User.Companion.fromDomain
import g.sig.data.entities.toDomain
import g.sig.data.nearby.entities.DiscoverState
import g.sig.domain.entities.Device
import g.sig.domain.entities.NearbyAction
import g.sig.domain.entities.User
import g.sig.domain.repositories.NearbyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import g.sig.domain.entities.ConnectionState as DomainConnectionState
import g.sig.domain.entities.Game as DomainGame

class NearbyRepositoryImpl(
    private val nearbyDataSource: NearbyDataSource,
    private val recentGamesDataStore: RecentGamesDataSource
) : NearbyRepository {
    override suspend fun getRecentGames(): List<DomainGame> = recentGamesDataStore.getRecentGames().map { it.toDomain() }

    override fun discoverNearbyDevices(user: User): Flow<NearbyAction> =
        nearbyDataSource.discover(user.fromDomain())
            .map {
                when (it) {
                    is DiscoverState.Discovered -> NearbyAction.AddDevice(Device(it.endpointId, it.info.endpointName, it.toDomain()))
                    is DiscoverState.Lost -> NearbyAction.RemoveDevice(it.endpointId)
                    else -> null
                }
            }
            .filterNotNull()

    override fun advertiseGame(game: String): Flow<DomainConnectionState> =
        nearbyDataSource.advertise(game).map { it.toDomain() }

    override fun cancelAdvertisement() = nearbyDataSource.cancelAdvertisement()

    override fun requestConnection(user: User, endpointId: String): Flow<DomainConnectionState> =
        nearbyDataSource.requestConnection(user.fromDomain(), endpointId).map { it.toDomain() }

    override fun acceptConnection(endpointId: String): Flow<DomainConnectionState> =
        nearbyDataSource.acceptConnection(endpointId).map { it.toDomain() }

    override fun rejectConnection(endpointId: String): Flow<DomainConnectionState> =
        nearbyDataSource.rejectConnection(endpointId).map { it.toDomain() }
}