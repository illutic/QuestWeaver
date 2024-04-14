package g.sig.data.nearby

import com.google.android.gms.nearby.connection.ConnectionInfo
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback
import com.google.android.gms.nearby.connection.ConnectionResolution
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.EmptyCoroutineContext

inline fun createConnectionCallback(
    crossinline onConnectionInitiated: suspend (endpointId: String, connectionInfo: ConnectionInfo) -> Unit,
    crossinline onConnectionResult: suspend (endpointId: String, connectionResolution: ConnectionResolution) -> Unit,
    crossinline onDisconnected: suspend (endpointId: String) -> Unit,
    coroutineScope: CoroutineScope = CoroutineScope(EmptyCoroutineContext),
) = object : ConnectionLifecycleCallback() {
    override fun onConnectionInitiated(
        endpointId: String,
        connectionInfo: ConnectionInfo,
    ) {
        coroutineScope.launch {
            onConnectionInitiated(endpointId, connectionInfo)
        }
    }

    override fun onConnectionResult(
        endpointId: String,
        connectionResolution: ConnectionResolution,
    ) {
        coroutineScope.launch {
            onConnectionResult(endpointId, connectionResolution)
        }
    }

    override fun onDisconnected(endpointId: String) {
        coroutineScope.launch {
            onDisconnected(endpointId)
        }
    }
}
