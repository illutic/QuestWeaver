package g.sig.data.repositories

import g.sig.common.data.toObjectOrNull
import g.sig.data.datasources.nearby.NearbyDataSource
import g.sig.data.datasources.nearby.PayloadCallback
import g.sig.data.datasources.recentgames.RecentGamesDataSource
import g.sig.data.entities.Game
import g.sig.data.entities.Game.Companion.fromDomain
import g.sig.data.entities.User.Companion.fromDomain
import g.sig.data.entities.asMessage
import g.sig.data.entities.toDomain
import g.sig.data.nearby.entities.ConnectionState
import g.sig.data.utils.logOnEach
import g.sig.domain.entities.NearbyAction
import g.sig.domain.entities.User
import g.sig.domain.repositories.NearbyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import g.sig.domain.entities.ConnectionState as DomainConnectionState
import g.sig.domain.entities.Game as DomainGame

class NearbyRepositoryImpl(
    private val nearbyDataSource: NearbyDataSource,
    private val recentGamesDataStore: RecentGamesDataSource,
    private val callback: PayloadCallback
) : NearbyRepository {
    override fun createGame(gameName: String, description: String, maxPlayers: Int): Flow<DomainConnectionState> {
        // The id is the endpointId of the host device, it is passed on the hostGame function
        val game = Game(id = "", title = gameName, description = description, maxPlayers = maxPlayers)
        return nearbyDataSource.hostGame(game).map { it.toDomain() }
    }

    override suspend fun getRecentGames(): List<DomainGame> = recentGamesDataStore.getRecentGames().map { it.toDomain() }

    override fun findNearbyGames(user: User): Flow<NearbyAction> =
        nearbyDataSource
            .findNearbyDevices(user.fromDomain())
            .combine(callback.data) { state, data ->
                when (state) {
                    is ConnectionState.Connected -> data.asMessage()?.content?.toObjectOrNull(Game.serializer())?.let { game ->
                        NearbyAction.AddGame(game.toDomain().copy(state.endpointId))
                    }

                    is ConnectionState.Disconnected -> NearbyAction.RemoveGame(state.endpointId)
                    else -> null
                }
            }
            .logOnEach()
            .filterNotNull()

    override fun joinGame(user: User, game: DomainGame): Flow<DomainConnectionState> =
        nearbyDataSource.joinGame(user.fromDomain(), game.fromDomain()).map { it.toDomain() }
}