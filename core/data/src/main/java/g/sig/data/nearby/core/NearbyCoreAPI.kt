package g.sig.data.nearby.core

import android.content.Context
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.connection.AdvertisingOptions
import com.google.android.gms.nearby.connection.ConnectionsStatusCodes
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo
import com.google.android.gms.nearby.connection.DiscoveryOptions
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback
import com.google.android.gms.nearby.connection.Strategy
import g.sig.data.nearby.entities.AdvertiseState
import g.sig.data.nearby.entities.ConnectionState
import g.sig.data.nearby.entities.DiscoverState
import g.sig.data.nearby.utils.createConnectionCallback
import g.sig.data.nearby.utils.doOnFailure
import g.sig.data.nearby.utils.doOnSuccess
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

fun requestConnection(
    context: Context,
    user: String,
    endpointId: String,
) = callbackFlow {
    val connectionLifecycleCallback =
        createConnectionCallback(
            onConnectionInitiated = { _, connectionInfo ->
                send(ConnectionState.Initiated(connectionInfo))
            },
            onConnectionResult = { endpointId, connectionResolution ->
                when (connectionResolution.status.statusCode) {
                    ConnectionsStatusCodes.STATUS_OK -> {
                        send(ConnectionState.Connected(endpointId))
                    }

                    ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED -> {
                        send(ConnectionState.Rejected(endpointId))
                    }

                    ConnectionsStatusCodes.STATUS_ERROR -> {
                        send(ConnectionState.Error(connectionResolution.status.statusMessage))
                    }
                }
            },
            onDisconnected = { _ ->
                send(ConnectionState.Disconnected)
            },
        )

    Nearby.getConnectionsClient(context.applicationContext)
        .requestConnection(
            user,
            endpointId,
            connectionLifecycleCallback,
        )
        .doOnSuccess { send(DiscoverState.ConnectionRequested) }
        .doOnFailure { send(DiscoverState.ConnectionRequestFailed) }

    awaitClose {
        Nearby.getConnectionsClient(context.applicationContext).stopDiscovery()
    }
}

fun startAdvertising(
    context: Context,
    name: String
): Flow<ConnectionState> =
    callbackFlow {
        send(ConnectionState.Idle)

        val options =
            AdvertisingOptions.Builder()
                .setStrategy(Strategy.P2P_CLUSTER)
                .build()

        val connectionLifecycleCallback =
            createConnectionCallback(
                onConnectionInitiated = { _, connectionInfo ->
                    send(ConnectionState.Initiated(connectionInfo))
                },
                onConnectionResult = { endpointId, connectionResolution ->
                    when (connectionResolution.status.statusCode) {
                        ConnectionsStatusCodes.STATUS_OK -> {
                            send(ConnectionState.Connected(endpointId))
                        }

                        ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED -> {
                            send(ConnectionState.Rejected(endpointId))
                        }

                        ConnectionsStatusCodes.STATUS_ERROR -> {
                            send(ConnectionState.Error(connectionResolution.status.statusMessage))
                        }
                    }
                },
                onDisconnected = { _ ->
                    send(ConnectionState.Disconnected)
                },
            )

        Nearby.getConnectionsClient(context.applicationContext)
            .startAdvertising(
                name,
                context.packageName,
                connectionLifecycleCallback,
                options,
            )
            .doOnSuccess { send(AdvertiseState.Advertising) }
            .doOnFailure { send(ConnectionState.Failure(it)) }

        awaitClose {
            Nearby.getConnectionsClient(context.applicationContext).stopAdvertising()
        }
    }

fun startDiscovery(
    context: Context,
    isLowPower: Boolean = false,
) = callbackFlow {
    send(ConnectionState.Idle)

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
                    send(DiscoverState.Discovered(endpointId, info))
                }
            }

            override fun onEndpointLost(endpointId: String) {
                this@callbackFlow.launch {
                    send(DiscoverState.Lost(endpointId))
                }
            }
        }

    Nearby.getConnectionsClient(context)
        .startDiscovery(
            context.packageName,
            callback,
            options,
        )
        .doOnSuccess { send(DiscoverState.Discovering) }
        .doOnFailure { send(ConnectionState.Failure(it)) }

    awaitClose {
        Nearby.getConnectionsClient(context.applicationContext).stopDiscovery()
    }
}