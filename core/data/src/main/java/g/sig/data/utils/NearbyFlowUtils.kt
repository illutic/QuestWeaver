package g.sig.data.utils

import com.google.android.gms.nearby.connection.ConnectionsClient
import com.google.android.gms.nearby.connection.PayloadCallback
import g.sig.data.nearby.entities.ConnectionState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach

fun Flow<ConnectionState>.acceptConnectionOnInitiated(
    connectionsClient: ConnectionsClient,
    payloadCallback: PayloadCallback
) = onEach {
    if (it is ConnectionState.Initiated) {
        connectionsClient.acceptConnection(it.endpointId, payloadCallback)
    }
}

inline fun Flow<ConnectionState>.doOnSuccess(
    crossinline block: suspend (ConnectionState.Connected) -> Unit
) = onEach {
    if (it is ConnectionState.Connected) {
        block(it)
    }
}

fun <T> Flow<T>.logOnEach() = onEach {
    println(it)
}