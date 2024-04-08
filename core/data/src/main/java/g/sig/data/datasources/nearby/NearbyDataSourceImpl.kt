package g.sig.data.datasources.nearby

import com.google.android.gms.nearby.connection.ConnectionsClient
import g.sig.data.entities.User
import g.sig.data.nearby.core.acceptConnection
import g.sig.data.nearby.core.rejectConnection
import g.sig.data.nearby.core.requestConnection
import g.sig.data.nearby.core.startAdvertising
import g.sig.data.nearby.core.startDiscovery
import g.sig.data.nearby.core.stopAdvertising
import g.sig.data.nearby.entities.ConnectionState
import g.sig.data.platform.BatteryLevel
import g.sig.data.platform.BatteryLevelReceiver
import g.sig.data.utils.acceptConnectionOnInitiated
import g.sig.data.utils.logOnEach
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapMerge

class NearbyDataSourceImpl(
    private val connectionsClient: ConnectionsClient,
    private val payloadCallback: PayloadCallback,
    private val serviceId: String
) : NearbyDataSource {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun discover(user: User): Flow<ConnectionState> =
        BatteryLevelReceiver.level
            .flatMapMerge { batteryLevel -> startDiscovery(connectionsClient, serviceId, batteryLevel == BatteryLevel.Low) }
            .logOnEach()

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun advertise(name: String): Flow<ConnectionState> =
        BatteryLevelReceiver.level
            .flatMapMerge { batteryLevel -> startAdvertising(connectionsClient, name, serviceId, batteryLevel == BatteryLevel.Low) }
            .logOnEach()

    override fun cancelAdvertisement() = stopAdvertising(connectionsClient)

    override fun requestConnection(user: User, endpointId: String): Flow<ConnectionState> =
        requestConnection(connectionsClient, user.name, endpointId)
            .acceptConnectionOnInitiated(connectionsClient, payloadCallback)

    override fun acceptConnection(endpointId: String): Flow<ConnectionState> = acceptConnection(connectionsClient, endpointId, payloadCallback)

    override fun rejectConnection(endpointId: String): Flow<ConnectionState> = rejectConnection(connectionsClient, endpointId)
}