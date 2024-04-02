package g.sig.data.datasources.nearby

import android.content.Context
import g.sig.data.entities.ConnectionState
import g.sig.data.entities.ConnectionState.Companion.fromNearby
import g.sig.data.entities.Game
import g.sig.data.entities.User
import g.sig.data.nearby.core.requestConnection
import g.sig.data.nearby.core.startDiscovery
import g.sig.data.nearby.entities.DiscoverState
import g.sig.data.platform.BatteryLevel
import g.sig.data.platform.BatteryLevelReceiver
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.protobuf.ProtoBuf

class NearbyGameDataSourceImpl(private val context: Context) : NearbyGameDataSource {
    @OptIn(ExperimentalCoroutinesApi::class, ExperimentalSerializationApi::class)
    override fun findNearbyGames(): Flow<Game> = BatteryLevelReceiver.level.flatMapMerge { batteryLevel ->
        startDiscovery(context, batteryLevel == BatteryLevel.Low)
    }
        .filterIsInstance<DiscoverState.Discovered>()
        .map {
            val payload = ProtoBuf.decodeFromByteArray<Game>(it.info.endpointInfo)
            payload.copy(id = it.endpointId)
        }

    override fun joinGame(user: User, game: Game): Flow<ConnectionState> = requestConnection(context, user.name, game.id).map { it.fromNearby() }
}