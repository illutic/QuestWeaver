package g.sig.data.nearby

import com.google.android.gms.common.api.ApiException
import com.google.android.gms.nearby.connection.AdvertisingOptions
import com.google.android.gms.nearby.connection.ConnectionsClient
import com.google.android.gms.nearby.connection.ConnectionsStatusCodes
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo
import com.google.android.gms.nearby.connection.DiscoveryOptions
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback
import com.google.android.gms.nearby.connection.Payload
import com.google.android.gms.nearby.connection.PayloadCallback
import com.google.android.gms.nearby.connection.Strategy
import g.sig.domain.entities.ConnectionState
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import java.io.InputStream

fun requestConnection(
    client: ConnectionsClient,
    user: String,
    endpointId: String
) = callbackFlow {
    trySend(ConnectionState.Loading)

    val connectionLifecycleCallback =
        createConnectionCallback(
            onConnectionInitiated = { endpointId, connectionInfo ->
                trySend(ConnectionState.Connecting(endpointId, connectionInfo.endpointName))
            },
            onConnectionResult = { endpointId, connectionResolution ->
                when (connectionResolution.status.statusCode) {
                    ConnectionsStatusCodes.STATUS_OK -> {
                        trySend(ConnectionState.Connected(endpointId))
                    }

                    ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED -> {
                        trySend(ConnectionState.Error.RejectError(endpointId))
                    }

                    ConnectionsStatusCodes.STATUS_ERROR -> {
                        trySend(ConnectionState.Error.GenericError(endpointId, connectionResolution.status.statusMessage))
                    }
                }
            },
            onDisconnected = { endpointId ->
                trySend(ConnectionState.Error.DisconnectionError(endpointId))
            },
        )

    client
        .requestConnection(
            user,
            endpointId,
            connectionLifecycleCallback,
        )
        .doOnSuccess { trySend(ConnectionState.Loading) }
        .doOnFailure {
            if (it is ApiException) {
                when (it.statusCode) {
                    ConnectionsStatusCodes.STATUS_ALREADY_CONNECTED_TO_ENDPOINT -> trySend(ConnectionState.Connected(endpointId))
                    ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED -> trySend(ConnectionState.Error.RejectError(endpointId))
                    else -> trySend(ConnectionState.Error.GenericError(endpointId, it.message))
                }
            }
        }

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
    trySend(ConnectionState.Loading)

    val options =
        AdvertisingOptions.Builder()
            .setStrategy(Strategy.P2P_CLUSTER)
            .setLowPower(isLowPower)
            .build()

    val connectionLifecycleCallback =
        createConnectionCallback(
            onConnectionInitiated = { endpointId, connectionInfo ->
                trySend(ConnectionState.Connecting(endpointId, connectionInfo.endpointName))
            },
            onConnectionResult = { endpointId, connectionResolution ->
                when (connectionResolution.status.statusCode) {
                    ConnectionsStatusCodes.STATUS_OK -> {
                        trySend(ConnectionState.Connected(endpointId))
                    }

                    ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED -> {
                        trySend(ConnectionState.Error.RejectError(endpointId))
                    }

                    ConnectionsStatusCodes.STATUS_ERROR -> {
                        trySend(ConnectionState.Error.GenericError(endpointId, connectionResolution.status.statusMessage))
                    }
                }
            },
            onDisconnected = { endpointId ->
                trySend(ConnectionState.Error.DisconnectionError(endpointId))
            },
        )

    client
        .startAdvertising(
            name,
            serviceId,
            connectionLifecycleCallback,
            options,
        )
        .doOnSuccess { trySend(ConnectionState.Loading) }
        .doOnFailure {
            if (it is ApiException) {
                when (it.statusCode) {
                    ConnectionsStatusCodes.STATUS_ALREADY_ADVERTISING -> trySend(ConnectionState.Loading)
                    else -> trySend(ConnectionState.Error.GenericError("", it.message))
                }
            }
        }

    awaitClose {
        client.stopAdvertising()
    }
}

fun stopAdvertising(client: ConnectionsClient) = client.stopAdvertising()

fun startDiscovery(
    client: ConnectionsClient,
    serviceId: String,
    isLowPower: Boolean = false,
) = callbackFlow {
    trySend(ConnectionState.Loading)

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
                    trySend(ConnectionState.Found(endpointId, info.endpointName))
                }
            }

            override fun onEndpointLost(endpointId: String) {
                this@callbackFlow.launch {
                    trySend(ConnectionState.Error.LostError(endpointId))
                }
            }
        }

    client
        .startDiscovery(
            serviceId,
            callback,
            options,
        )
        .doOnSuccess { trySend(ConnectionState.Loading) }
        .doOnFailure {
            if (it is ApiException) {
                when (it.statusCode) {
                    ConnectionsStatusCodes.STATUS_ALREADY_DISCOVERING -> trySend(ConnectionState.Loading)
                    else -> trySend(ConnectionState.Error.ConnectionRequestError)
                }
            }
        }

    awaitClose { client.stopDiscovery() }
}

fun acceptConnection(
    client: ConnectionsClient,
    endpointId: String,
    payloadCallback: PayloadCallback
) = callbackFlow {
    trySend(ConnectionState.Loading)

    client
        .acceptConnection(endpointId, payloadCallback)
        .doOnSuccess {
            trySend(ConnectionState.Connected(endpointId))
        }
        .doOnFailure {
            if (it is ApiException) {
                when (it.statusCode) {
                    ConnectionsStatusCodes.STATUS_ALREADY_CONNECTED_TO_ENDPOINT -> trySend(ConnectionState.Connected(endpointId))
                    else -> trySend(ConnectionState.Error.GenericError(endpointId, it.message))
                }
            }
        }
    awaitClose()
}

fun rejectConnection(
    client: ConnectionsClient,
    endpointId: String
) = callbackFlow {
    client
        .rejectConnection(endpointId)
        .doOnSuccess {
            trySend(ConnectionState.Error.RejectError(endpointId))
        }
        .doOnFailure {
            trySend(ConnectionState.Error.GenericError(endpointId, it.message))
        }
    awaitClose()
}

fun ConnectionsClient.sendPayload(
    endpointId: String,
    payload: InputStream,
) = sendPayload(endpointId, Payload.fromStream(payload))

fun ConnectionsClient.sendPayload(
    endpointId: String,
    payload: ByteArray,
) = sendPayload(endpointId, Payload.fromBytes(payload))