package g.sig.questweaver.data.repositories

import android.content.Context
import com.google.android.gms.nearby.connection.ConnectionsClient
import g.sig.questweaver.data.datasources.nearby.PayloadCallback
import g.sig.questweaver.data.entities.DataEntity
import g.sig.questweaver.data.entities.FileMetadata.Companion.fromDomain
import g.sig.questweaver.data.entities.Game.Companion.fromDomain
import g.sig.questweaver.data.entities.User.Companion.fromDomain
import g.sig.questweaver.data.mapper.toDomainData
import g.sig.questweaver.data.nearby.sendPayload
import g.sig.questweaver.data.serializers.ProtoBufSerializer
import g.sig.questweaver.data.utils.toUri
import g.sig.questweaver.domain.entities.File
import g.sig.questweaver.domain.entities.FileMetadata
import g.sig.questweaver.domain.entities.Game
import g.sig.questweaver.domain.entities.PayloadData
import g.sig.questweaver.domain.entities.Stream
import g.sig.questweaver.domain.entities.User
import g.sig.questweaver.domain.repositories.PayloadRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.ExperimentalSerializationApi

class PayloadRepositoryImpl(
    private val context: Context,
    private val connectionsClient: ConnectionsClient,
    payloadCallback: PayloadCallback
) : PayloadRepository {
    override val data: Flow<PayloadData> = payloadCallback.data.map { it.toDomainData() }

    @OptIn(ExperimentalSerializationApi::class)
    private fun ConnectionsClient.sendPayloadData(endpointId: String, data: PayloadData) {
        when (data) {
            is File -> context.contentResolver.openInputStream(
                data.uri.toUri()
            )?.use { inputStream -> sendPayload(endpointId, inputStream) }

            is Stream -> data.inputStream.use { inputStream ->
                sendPayload(
                    endpointId,
                    inputStream
                )
            }

            is FileMetadata -> sendPayload(
                endpointId,
                ProtoBufSerializer.encodeToByteArray(DataEntity.serializer(), data.fromDomain())
            )

            is Game -> sendPayload(
                endpointId,
                ProtoBufSerializer.encodeToByteArray(
                    DataEntity.serializer(),
                    data.fromDomain().copy(isDM = false)
                )
            )

            is User -> sendPayload(
                endpointId,
                ProtoBufSerializer.encodeToByteArray(DataEntity.serializer(), data.fromDomain())
            )
        }
    }

    override fun send(endpointId: String, data: PayloadData) {
        connectionsClient.sendPayloadData(endpointId, data)
    }
}
