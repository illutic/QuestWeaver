package g.sig.data.datasources.nearby

import android.content.Context
import com.google.android.gms.nearby.connection.ConnectionsClient
import g.sig.data.entities.DataEntity
import g.sig.data.entities.User
import g.sig.data.entities.asData
import g.sig.data.nearby.core.acceptConnection
import g.sig.data.nearby.core.rejectConnection
import g.sig.data.nearby.core.requestConnection
import g.sig.data.nearby.core.sendPayload
import g.sig.data.nearby.core.startAdvertising
import g.sig.data.nearby.core.startDiscovery
import g.sig.data.nearby.core.stopAdvertising
import g.sig.data.nearby.entities.ConnectionState
import g.sig.data.platform.BatteryLevel
import g.sig.data.platform.BatteryLevelReceiver
import g.sig.data.utils.acceptConnectionOnInitiated
import g.sig.data.utils.logOnEach
import g.sig.domain.entities.Device
import g.sig.domain.repositories.DeviceRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapMerge

class NearbyDataSourceImpl(
    private val context: Context,
    private val deviceRepository: DeviceRepository,
    private val connectionsClient: ConnectionsClient,
    private val payloadCallback: PayloadCallback,
    private val serviceId: String
) : NearbyDataSource {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun discover(user: User): Flow<ConnectionState> =
        BatteryLevelReceiver
            .level
            .flatMapMerge { batteryLevel -> startDiscovery(connectionsClient, serviceId, batteryLevel == BatteryLevel.Low) }
            .logOnEach()

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun advertise(name: String): Flow<ConnectionState> =
        BatteryLevelReceiver
            .level
            .flatMapMerge { batteryLevel -> startAdvertising(connectionsClient, name, serviceId, batteryLevel == BatteryLevel.Low) }
            .logOnEach()

    override fun cancelAdvertisement() = stopAdvertising(connectionsClient)

    override fun requestConnection(user: User, endpointId: String): Flow<ConnectionState> =
        requestConnection(connectionsClient, user.name, endpointId)
            .acceptConnectionOnInitiated(connectionsClient, payloadCallback)
            .logOnEach()

    override fun acceptConnection(endpointId: String): Flow<ConnectionState> =
        acceptConnection(connectionsClient, endpointId, payloadCallback)
            .logOnEach()

    override fun rejectConnection(endpointId: String): Flow<ConnectionState> =
        rejectConnection(connectionsClient, endpointId)
            .logOnEach()

    override fun sendData(device: Device, dataEntity: DataEntity) {
        connectionsClient.sendPayload(context, device.id, dataEntity.asData())
    }

    override fun broadcast(dataEntity: DataEntity) {
        deviceRepository.getDevices().forEach { device ->
            connectionsClient.sendPayload(context, device.id, dataEntity.asData())
        }
    }
}