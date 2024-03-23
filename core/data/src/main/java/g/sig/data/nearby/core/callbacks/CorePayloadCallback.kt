package g.sig.data.nearby.core.callbacks

import com.google.android.gms.nearby.connection.Payload
import com.google.android.gms.nearby.connection.PayloadCallback
import com.google.android.gms.nearby.connection.PayloadTransferUpdate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class CorePayloadCallback(coroutineScope: CoroutineScope) :
    PayloadCallback(),
    CoroutineScope by coroutineScope {
    private val innerPayload = Channel<Payload>(Channel.BUFFERED)
    private val innerTransferUpdate = Channel<PayloadTransferUpdate>(Channel.BUFFERED)
    val payload = innerPayload.consumeAsFlow()
    val transferUpdate = innerTransferUpdate.consumeAsFlow()

    override fun onPayloadReceived(
        endpointId: String,
        payload: Payload,
    ) {
        launch { innerPayload.send(payload) }
    }

    override fun onPayloadTransferUpdate(
        endpointId: String,
        payloadUpdate: PayloadTransferUpdate,
    ) {
        launch { innerTransferUpdate.send(payloadUpdate) }
    }
}
