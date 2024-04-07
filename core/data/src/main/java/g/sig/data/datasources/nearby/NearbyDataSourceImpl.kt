package g.sig.data.datasources.nearby

import android.content.Context
import com.google.android.gms.nearby.connection.ConnectionsClient
import g.sig.data.entities.Game
import g.sig.data.entities.User
import g.sig.data.nearby.core.requestConnection
import g.sig.data.nearby.core.sendPayload
import g.sig.data.nearby.core.startAdvertising
import g.sig.data.nearby.core.startDiscovery
import g.sig.data.nearby.entities.ConnectionState
import g.sig.data.nearby.entities.Data
import g.sig.data.nearby.entities.DiscoverState
import g.sig.data.platform.BatteryLevel
import g.sig.data.platform.BatteryLevelReceiver
import g.sig.data.utils.acceptConnectionOnInitiated
import g.sig.data.utils.doOnSuccess
import g.sig.data.utils.logOnEach
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.protobuf.ProtoBuf

class NearbyDataSourceImpl(
    private val context: Context,
    private val connectionsClient: ConnectionsClient,
    private val payloadCallback: PayloadCallback,
    private val serviceId: String
) : NearbyDataSource {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun findNearbyDevices(user: User): Flow<ConnectionState> =
        BatteryLevelReceiver.level
            .flatMapMerge { batteryLevel -> startDiscovery(connectionsClient, serviceId, batteryLevel == BatteryLevel.Low) }
            .flatMapMerge {
                if (it is DiscoverState.Discovered) requestConnection(connectionsClient, user.name, it.endpointId)
                else emptyFlow()
            }
            .acceptConnectionOnInitiated(connectionsClient, payloadCallback)
            .logOnEach()

    override fun joinGame(user: User, game: Game): Flow<ConnectionState> =
        requestConnection(connectionsClient, user.name, game.id)
            .acceptConnectionOnInitiated(connectionsClient, payloadCallback)

    @OptIn(ExperimentalCoroutinesApi::class, ExperimentalSerializationApi::class)
    override fun hostGame(game: Game): Flow<ConnectionState> =
        BatteryLevelReceiver.level
            .flatMapMerge { batteryLevel -> startAdvertising(connectionsClient, game.title, serviceId, batteryLevel == BatteryLevel.Low) }
            .acceptConnectionOnInitiated(connectionsClient, payloadCallback)
            .doOnSuccess {
                connectionsClient.sendPayload(context, it.endpointId, Data.Message(ProtoBuf.encodeToByteArray(game)))
            }
            .logOnEach()
}