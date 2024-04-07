package g.sig.data.nearby.core

import android.content.Context
import com.google.android.gms.nearby.connection.AdvertisingOptions
import com.google.android.gms.nearby.connection.ConnectionsClient
import com.google.android.gms.nearby.connection.ConnectionsStatusCodes
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo
import com.google.android.gms.nearby.connection.DiscoveryOptions
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback
import com.google.android.gms.nearby.connection.Payload
import com.google.android.gms.nearby.connection.Strategy
import g.sig.data.nearby.entities.AdvertiseState
import g.sig.data.nearby.entities.ConnectionState
import g.sig.data.nearby.entities.Data
import g.sig.data.nearby.entities.DiscoverState
import g.sig.data.nearby.utils.createConnectionCallback
import g.sig.data.nearby.utils.doOnFailure
import g.sig.data.nearby.utils.doOnSuccess
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import java.io.InputStream

fun requestConnection(
    client: ConnectionsClient,
    user: String,
    endpointId: String
) = callbackFlow {
    val connectionLifecycleCallback =
        createConnectionCallback(
            onConnectionInitiated = { endpointId, connectionInfo ->
                trySend(ConnectionState.Initiated(endpointId, connectionInfo))
            },
            onConnectionResult = { endpointId, connectionResolution ->
                when (connectionResolution.status.statusCode) {
                    ConnectionsStatusCodes.STATUS_OK -> {
                        trySend(ConnectionState.Connected(endpointId))
                    }

                    ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED -> {
                        trySend(ConnectionState.Rejected(endpointId))
                    }

                    ConnectionsStatusCodes.STATUS_ERROR -> {
                        trySend(ConnectionState.Error(endpointId, connectionResolution.status.statusMessage))
                    }
                }
            },
            onDisconnected = { endpointId ->
                trySend(ConnectionState.Disconnected(endpointId))
            },
        )

    client
        .requestConnection(
            user,
            endpointId,
            connectionLifecycleCallback,
        )
        .doOnSuccess { trySend(DiscoverState.ConnectionRequested) }
        .doOnFailure { trySend(DiscoverState.ConnectionRequestFailed) }

    awaitClose {
        client.stopDiscovery()
    }
}

fun startAdvertising(
    client: ConnectionsClient,
    name: String,
    serviceId: String,
    isLowPower: Boolean = false,
) = callbackFlow {
    val options =
        AdvertisingOptions.Builder()
            .setStrategy(Strategy.P2P_CLUSTER)
            .setLowPower(isLowPower)
            .build()

    val connectionLifecycleCallback =
        createConnectionCallback(
            onConnectionInitiated = { endpointId, connectionInfo ->
                trySend(ConnectionState.Initiated(endpointId, connectionInfo))
            },
            onConnectionResult = { endpointId, connectionResolution ->
                when (connectionResolution.status.statusCode) {
                    ConnectionsStatusCodes.STATUS_OK -> {
                        trySend(ConnectionState.Connected(endpointId))
                    }

                    ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED -> {
                        trySend(ConnectionState.Rejected(endpointId))
                    }

                    ConnectionsStatusCodes.STATUS_ERROR -> {
                        trySend(ConnectionState.Error(endpointId, connectionResolution.status.statusMessage))
                    }
                }
            },
            onDisconnected = { endpointId ->
                trySend(ConnectionState.Disconnected(endpointId))
            },
        )

    client
        .startAdvertising(
            name,
            serviceId,
            connectionLifecycleCallback,
            options,
        )
        .doOnSuccess { trySend(AdvertiseState.Advertising) }
        .doOnFailure {
            trySend(ConnectionState.Failure(it))
            close()
        }

    awaitClose {
        client.stopAdvertising()
    }
}

fun startDiscovery(
    client: ConnectionsClient,
    serviceId: String,
    isLowPower: Boolean = false,
) = callbackFlow {
    val options =
        DiscoveryOptions.Builder()
            .setStrategy(Strategy.P2P_CLUSTER)
            .setLowPower(isLowPower)
            .build()

    val callback =
        object : EndpointDiscoveryCallback() {
            override fun onEndpointFound(
                endpointId: String,
                info: DiscoveredEndpointInfo,
            ) {
                this@callbackFlow.launch {
                    trySend(DiscoverState.Discovered(endpointId, info))
                }
            }

            override fun onEndpointLost(endpointId: String) {
                this@callbackFlow.launch {
                    trySend(DiscoverState.Lost(endpointId))
                }
            }
        }

    client
        .startDiscovery(
            serviceId,
            callback,
            options,
        )
        .doOnSuccess { trySend(DiscoverState.Discovering) }
        .doOnFailure { trySend(ConnectionState.Failure(it)) }

    awaitClose { client.stopDiscovery() }
}

fun ConnectionsClient.sendPayload(
    context: Context,
    endpointId: String,
    data: Data,
) {
    when (data) {
        is Data.File -> context.contentResolver.openInputStream(data.uri)?.use { sendPayload(endpointId, it) }
        is Data.Message -> sendPayload(endpointId, data.byteArray)
        is Data.Stream -> sendPayload(endpointId, data.inputStream)
    }
}

private fun ConnectionsClient.sendPayload(
    endpointId: String,
    payload: InputStream,
) = sendPayload(endpointId, Payload.fromStream(payload))

private fun ConnectionsClient.sendPayload(
    endpointId: String,
    payload: ByteArray,
) = sendPayload(endpointId, Payload.fromBytes(payload))