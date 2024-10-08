package g.sig.questweaver.data.utils

import com.google.android.gms.nearby.connection.ConnectionsClient
import com.google.android.gms.nearby.connection.PayloadCallback
import g.sig.questweaver.domain.entities.states.ConnectionState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach

fun Flow<ConnectionState>.acceptConnectionOnInitiated(
    connectionsClient: ConnectionsClient,
    payloadCallback: PayloadCallback,
) = logOnEach()
    .onEach {
        if (it is ConnectionState.Connecting) {
            connectionsClient.acceptConnection(it.endpointId, payloadCallback)
        }
    }

fun <T> Flow<T>.logOnEach() =
    onEach {
        println(it)
    }
