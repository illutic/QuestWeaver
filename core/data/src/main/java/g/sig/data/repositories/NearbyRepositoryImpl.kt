package g.sig.data.repositories

import g.sig.common.utils.addOrReplace
import g.sig.common.utils.update
import g.sig.data.datasources.nearby.NearbyDataSource
import g.sig.data.datasources.recentgames.RecentGamesDataSource
import g.sig.data.entities.User.Companion.fromDomain
import g.sig.data.entities.toDomain
import g.sig.data.nearby.entities.ConnectionState
import g.sig.data.nearby.entities.DiscoverState
import g.sig.domain.entities.Device
import g.sig.domain.entities.Device.Companion.hasTheSameIdAs
import g.sig.domain.entities.Device.Companion.hasTheSameNameAs
import g.sig.domain.entities.Device.Companion.isSimilarTo
import g.sig.domain.entities.User
import g.sig.domain.repositories.NearbyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import g.sig.domain.entities.ConnectionState as DomainConnectionState
import g.sig.domain.entities.Game as DomainGame

class NearbyRepositoryImpl(
    private val nearbyDataSource: NearbyDataSource,
    private val recentGamesDataStore: RecentGamesDataSource
) : NearbyRepository {
    private val storedDevices = mutableListOf<Device>()
    override suspend fun getRecentGames(): List<DomainGame> = recentGamesDataStore.getRecentGames().map { it.toDomain() }

    override fun discoverNearbyDevices(user: User): Flow<List<Device>> =
        nearbyDataSource.discover(user.fromDomain())
            .onEach { state ->
                when (state) {
                    is DiscoverState.Discovered -> storedDevices.addOrReplace(
                        { it hasTheSameNameAs state.name || it hasTheSameIdAs state.endpointId },
                        Device(state.endpointId, state.name, DomainConnectionState.Idle)
                    )

                    is DiscoverState.Lost -> storedDevices.removeAll { it hasTheSameIdAs state.endpointId }

                    else -> storedDevices.update({ it hasTheSameIdAs state.endpointId }, { it.copy(connectionState = state.toDomain()) })
                }
            }
            .map { storedDevices }

    override fun advertiseGame(game: String): Flow<List<Device>> =
        nearbyDataSource.advertise(game)
            .onEach { state ->
                when (state) {
                    is ConnectionState.Initiated -> storedDevices.addOrReplace(
                        { it hasTheSameNameAs state.name || it hasTheSameIdAs state.endpointId },
                        Device(state.endpointId, state.name, DomainConnectionState.Idle)
                    )

                    is ConnectionState.Disconnected -> storedDevices.removeAll { it hasTheSameIdAs state.endpointId }

                    else -> storedDevices.update({ it hasTheSameIdAs state.endpointId }, { it.copy(connectionState = state.toDomain()) })
                }
            }
            .map { storedDevices }

    override fun cancelAdvertisement() = nearbyDataSource.cancelAdvertisement()

    override fun requestConnection(user: User, device: Device): Flow<DomainConnectionState> =
        nearbyDataSource
            .requestConnection(user.fromDomain(), device.id)
            .onEach { state ->
                storedDevices.update({ it isSimilarTo device }, { it.copy(connectionState = state.toDomain()) })
            }
            .map { it.toDomain() }

    override fun acceptConnection(device: Device): Flow<DomainConnectionState> =
        nearbyDataSource.acceptConnection(device.id)
            .onEach { state ->
                storedDevices.update({ it isSimilarTo device }, { it.copy(connectionState = state.toDomain()) })
            }
            .map { it.toDomain() }

    override fun rejectConnection(device: Device): Flow<DomainConnectionState> =
        nearbyDataSource
            .rejectConnection(device.id)
            .onEach { state ->
                storedDevices.update({ it isSimilarTo device }, { it.copy(connectionState = state.toDomain()) })
            }
            .map { it.toDomain() }
}