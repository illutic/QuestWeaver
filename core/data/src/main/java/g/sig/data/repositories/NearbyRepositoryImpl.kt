package g.sig.data.repositories

import g.sig.data.datasources.nearby.NearbyDataSource
import g.sig.data.datasources.recentgames.RecentGamesDataSource
import g.sig.data.entities.User.Companion.fromDomain
import g.sig.data.entities.toDomain
import g.sig.data.nearby.entities.ConnectionState
import g.sig.data.nearby.entities.DiscoverState
import g.sig.domain.entities.Device
import g.sig.domain.entities.User
import g.sig.domain.repositories.DeviceRepository
import g.sig.domain.repositories.NearbyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import g.sig.domain.entities.ConnectionState as DomainConnectionState
import g.sig.domain.entities.Game as DomainGame

class NearbyRepositoryImpl(
    private val deviceRepository: DeviceRepository,
    private val nearbyDataSource: NearbyDataSource,
    private val recentGamesDataStore: RecentGamesDataSource
) : NearbyRepository {
    override suspend fun getRecentGames(): List<DomainGame> = recentGamesDataStore.getRecentGames().map { it.toDomain() }

    override fun discoverNearbyDevices(user: User): Flow<List<Device>> =
        nearbyDataSource.discover(user.fromDomain())
            .onEach { state ->
                when (state) {
                    is DiscoverState.Discovered -> deviceRepository.addDevice(state.endpointId, state.name)

                    is DiscoverState.Lost -> deviceRepository.removeDevice(state.endpointId)

                    else -> deviceRepository.updateState(state.endpointId, state.toDomain())
                }
            }
            .map { deviceRepository.getDevices() }

    override fun advertiseGame(game: String): Flow<List<Device>> =
        nearbyDataSource.advertise(game)
            .onEach { state ->
                when (state) {
                    is ConnectionState.Initiated -> deviceRepository.addDevice(state.endpointId, state.name)

                    is ConnectionState.Disconnected -> deviceRepository.removeDevice(state.endpointId)

                    else -> deviceRepository.updateState(state.endpointId, state.toDomain())
                }
            }
            .map { deviceRepository.getDevices() }

    override fun cancelAdvertisement() = nearbyDataSource.cancelAdvertisement()

    override fun requestConnection(user: User, device: Device): Flow<DomainConnectionState> =
        nearbyDataSource
            .requestConnection(user.fromDomain(), device.id)
            .onEach { deviceRepository.updateState(it.endpointId, it.toDomain()) }
            .map { it.toDomain() }

    override fun acceptConnection(device: Device): Flow<DomainConnectionState> =
        nearbyDataSource.acceptConnection(device.id)
            .onEach { deviceRepository.updateState(it.endpointId, it.toDomain()) }
            .map { it.toDomain() }

    override fun rejectConnection(device: Device): Flow<DomainConnectionState> =
        nearbyDataSource
            .rejectConnection(device.id)
            .onEach { deviceRepository.updateState(it.endpointId, it.toDomain()) }
            .map { it.toDomain() }
}