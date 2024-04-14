package g.sig.questweaver.data

import android.content.Context
import android.net.Uri
import com.google.android.gms.nearby.connection.Payload
import com.google.android.gms.nearby.connection.PayloadTransferUpdate
import g.sig.common.data.copyToCacheAndDelete
import g.sig.data.entities.DataEntity
import g.sig.data.entities.File
import g.sig.data.entities.FileMetadata
import g.sig.data.entities.Stream
import g.sig.data.serializers.ProtoBufSerializer
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromByteArray
import g.sig.data.datasources.nearby.PayloadCallback as NearbyPayloadCallback

class CorePayloadCallback(
    private val context: Context,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : NearbyPayloadCallback(), CoroutineScope by CoroutineScope(dispatcher) {
    private val incomingPayloads = mutableMapOf<Long, Payload>()
    private val completedFilePayloads = mutableMapOf<Long, Payload>()
    private val filePayloadMetadata = mutableMapOf<Long, FileMetadata>()
    private val _data = MutableSharedFlow<DataEntity>()
    override val data = _data.asSharedFlow()

    override fun onPayloadReceived(
        endpointId: String,
        payload: Payload,
    ) {
        when (payload.type) {
            Payload.Type.BYTES -> handleBytesPayload(payload)

            Payload.Type.FILE -> incomingPayloads[payload.id] = payload

            Payload.Type.STREAM -> {
                incomingPayloads[payload.id] = payload
                handleStreamPayload(payload)
            }
        }
    }

    override fun onPayloadTransferUpdate(
        endpointId: String,
        payloadUpdate: PayloadTransferUpdate,
    ) {
        if (payloadUpdate.status == PayloadTransferUpdate.Status.SUCCESS) {
            val payload = incomingPayloads.remove(payloadUpdate.payloadId) ?: return
            if (payload.type == Payload.Type.FILE) {
                completedFilePayloads[payloadUpdate.payloadId] = payload
                handleFilePayload(payloadUpdate.payloadId)
            }
        }
    }

    /**
     * Handles a payload of type [Payload.Type.BYTES].
     * If the payload contains a filename, it will be saved and the payload will be treated as a file.
     * Otherwise, the payload will be treated as a message.
     * @param payload The payload to handle.
     */
    @OptIn(ExperimentalSerializationApi::class)
    private fun handleBytesPayload(payload: Payload) {
        val message = ProtoBufSerializer.decodeFromByteArray<DataEntity>(payload.asBytes() ?: return)

        if (message is FileMetadata) {
            filePayloadMetadata[payload.id] = message
            return
        } else {
            launch { _data.emit(message) }
        }
    }

    /**
     * Handles a payload of type [Payload.Type.STREAM].
     * The payload will be treated as a stream will be sent as [Stream].
     * @param payload The payload to handle.
     */
    private fun handleStreamPayload(payload: Payload) {
        val inputStream = payload.asStream()?.asInputStream() ?: return
        launch { _data.emit(Stream(inputStream)) }
    }

    /**
     * Handles a payload of type [Payload.Type.FILE].
     * The payload will be saved to the cache and the URI will be sent as [DataEntity.File].
     * @param payloadId The ID of the payload to handle.
     */
    private fun handleFilePayload(payloadId: Long) {
        val payload = completedFilePayloads.remove(payloadId) ?: return
        val metadata = filePayloadMetadata.remove(payloadId) ?: return

        launch {
            payload.asFile()?.asUri()?.copyToCacheAndDelete(dispatcher, context, metadata.name)
            val uri = Uri.fromFile(context.cacheDir.resolve(metadata.name))
            _data.emit(File(uri, metadata))
        }
    }
}
