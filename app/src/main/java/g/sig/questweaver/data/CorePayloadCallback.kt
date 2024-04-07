package g.sig.questweaver.data

import android.content.Context
import android.net.Uri
import com.google.android.gms.nearby.connection.Payload
import com.google.android.gms.nearby.connection.PayloadTransferUpdate
import g.sig.data.entities.FileMetadata
import g.sig.data.entities.asDataEntity
import g.sig.data.nearby.entities.Data
import g.sig.data.nearby.utils.copyToCacheAndDelete
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.protobuf.ProtoBuf
import g.sig.data.datasources.nearby.PayloadCallback as NearbyPayloadCallback

class CorePayloadCallback(
    private val context: Context,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : NearbyPayloadCallback(), CoroutineScope by CoroutineScope(dispatcher) {
    private val incomingPayloads = mutableMapOf<Long, Payload>()
    private val completedFilePayloads = mutableMapOf<Long, Payload>()
    private val filePayloadMetadata = mutableMapOf<Long, FileMetadata>()
    private val _data = MutableSharedFlow<Data>()
    override val data = _data.asSharedFlow().map { it.asDataEntity() }

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
    private fun handleBytesPayload(payload: Payload) {
        val message = payload.asBytes()?.let { Data.Message(it) } ?: Data.Message.Empty

        val fileMetadata = try {
            ProtoBuf.decodeFromByteArray<FileMetadata?>(message.byteArray)
        } catch (e: Exception) {
            null
        }

        if (fileMetadata != null) {
            filePayloadMetadata[payload.id] = fileMetadata
            return
        } else {
            launch { _data.emit(message) }
        }
    }

    /**
     * Handles a payload of type [Payload.Type.STREAM].
     * The payload will be treated as a stream will be sent as [Data.Stream].
     * @param payload The payload to handle.
     */
    private fun handleStreamPayload(payload: Payload) {
        val inputStream = payload.asStream()?.asInputStream() ?: return
        launch { _data.emit(Data.Stream(inputStream)) }
    }

    /**
     * Handles a payload of type [Payload.Type.FILE].
     * The payload will be saved to the cache and the URI will be sent as [Data.File].
     * @param payloadId The ID of the payload to handle.
     */
    @OptIn(ExperimentalSerializationApi::class)
    private fun handleFilePayload(payloadId: Long) {
        val payload = completedFilePayloads.remove(payloadId) ?: return
        val metadata = filePayloadMetadata.remove(payloadId) ?: return

        launch {
            payload.asFile()?.asUri()?.copyToCacheAndDelete(dispatcher, context, metadata.name)
            val uri = Uri.fromFile(context.cacheDir.resolve(metadata.name))
            _data.emit(Data.File(uri = uri, fileMetadata = Data.Message(ProtoBuf.encodeToByteArray(metadata))))
        }
    }
}
