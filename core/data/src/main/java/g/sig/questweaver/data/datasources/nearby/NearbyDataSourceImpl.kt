package g.sig.questweaver.data.datasources.nearby

import com.google.android.gms.nearby.connection.ConnectionsClient
import g.sig.questweaver.data.entities.common.UserDto
import g.sig.questweaver.data.nearby.acceptConnection
import g.sig.questweaver.data.nearby.rejectConnection
import g.sig.questweaver.data.nearby.requestConnection
import g.sig.questweaver.data.nearby.startAdvertising
import g.sig.questweaver.data.nearby.startDiscovery
import g.sig.questweaver.data.nearby.stopAdvertising
import g.sig.questweaver.data.nearby.stopDiscovery
import g.sig.questweaver.data.platform.BatteryLevel
import g.sig.questweaver.data.platform.BatteryLevelReceiver
import g.sig.questweaver.data.utils.acceptConnectionOnInitiated
import g.sig.questweaver.data.utils.logOnEach
import g.sig.questweaver.domain.entities.states.ConnectionState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapMerge

class NearbyDataSourceImpl(
    private val connectionsClient: ConnectionsClient,
    private val payloadCallback: PayloadCallback,
    private val serviceId: String
) : NearbyDataSource {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun discover(userDto: UserDto): Flow<ConnectionState> =
        BatteryLevelReceiver
            .level
            .flatMapMerge { batteryLevel ->
                startDiscovery(
                    connectionsClient,
                    serviceId,
                    batteryLevel == BatteryLevel.Low
                )
            }
            .logOnEach()

    override fun cancelDiscovery() = stopDiscovery(connectionsClient)

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun advertise(name: String): Flow<ConnectionState> =
        BatteryLevelReceiver
            .level
            .flatMapMerge { batteryLevel ->
                startAdvertising(
                    connectionsClient,
                    name,
                    serviceId,
                    batteryLevel == BatteryLevel.Low
                )
            }
            .logOnEach()

    override fun cancelAdvertisement() = stopAdvertising(connectionsClient)

    override fun requestConnection(userDto: UserDto, endpointId: String): Flow<ConnectionState> =
        requestConnection(connectionsClient, userDto.name, endpointId)
            .acceptConnectionOnInitiated(connectionsClient, payloadCallback)
            .logOnEach()

    override fun acceptConnection(endpointId: String): Flow<ConnectionState> =
        acceptConnection(connectionsClient, endpointId, payloadCallback)
            .logOnEach()

    override fun rejectConnection(endpointId: String): Flow<ConnectionState> =
        rejectConnection(connectionsClient, endpointId)
            .logOnEach()
}
