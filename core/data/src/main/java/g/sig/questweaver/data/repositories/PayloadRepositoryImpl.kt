package g.sig.questweaver.data.repositories

import android.content.Context
import com.google.android.gms.nearby.connection.ConnectionsClient
import g.sig.questweaver.data.datasources.nearby.PayloadCallback
import g.sig.questweaver.data.entities.InteractionDto
import g.sig.questweaver.data.entities.InteractionDto.DrawingDto.Companion.fromDomain
import g.sig.questweaver.data.entities.InteractionDto.TextDto.Companion.fromDomain
import g.sig.questweaver.data.entities.blocks.ColorDto
import g.sig.questweaver.data.entities.blocks.ColorDto.Companion.fromDomain
import g.sig.questweaver.data.entities.blocks.PointDto
import g.sig.questweaver.data.entities.blocks.PointDto.Companion.fromDomain
import g.sig.questweaver.data.entities.blocks.SizeDto
import g.sig.questweaver.data.entities.blocks.SizeDto.Companion.fromDomain
import g.sig.questweaver.data.entities.common.GameDto
import g.sig.questweaver.data.entities.common.GameDto.Companion.fromDomain
import g.sig.questweaver.data.entities.common.UserDto
import g.sig.questweaver.data.entities.common.UserDto.Companion.fromDomain
import g.sig.questweaver.data.entities.io.FileMetadataDto
import g.sig.questweaver.data.entities.io.FileMetadataDto.Companion.fromDomain
import g.sig.questweaver.data.entities.states.GameHomeStateDto
import g.sig.questweaver.data.entities.states.GameHomeStateDto.Companion.fromDomain
import g.sig.questweaver.data.entities.states.GameStateDto
import g.sig.questweaver.data.entities.states.GameStateDto.Companion.fromDomain
import g.sig.questweaver.data.mapper.toDomainData
import g.sig.questweaver.data.nearby.sendPayload
import g.sig.questweaver.data.serializers.ProtoBufSerializer
import g.sig.questweaver.data.utils.toUri
import g.sig.questweaver.domain.entities.Interaction
import g.sig.questweaver.domain.entities.blocks.Color
import g.sig.questweaver.domain.entities.blocks.Point
import g.sig.questweaver.domain.entities.blocks.Size
import g.sig.questweaver.domain.entities.common.Game
import g.sig.questweaver.domain.entities.common.User
import g.sig.questweaver.domain.entities.io.File
import g.sig.questweaver.domain.entities.io.FileMetadata
import g.sig.questweaver.domain.entities.io.PayloadData
import g.sig.questweaver.domain.entities.io.Stream
import g.sig.questweaver.domain.entities.states.GameHomeState
import g.sig.questweaver.domain.entities.states.GameState
import g.sig.questweaver.domain.repositories.PayloadRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToByteArray

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
                ProtoBufSerializer.encodeToByteArray<FileMetadataDto>(data.fromDomain())
            )

            is Game -> sendPayload(
                endpointId,
                ProtoBufSerializer.encodeToByteArray<GameDto>(data.fromDomain())
            )

            is User -> sendPayload(
                endpointId,
                ProtoBufSerializer.encodeToByteArray<UserDto>(data.fromDomain())
            )

            is Color -> sendPayload(
                endpointId,
                ProtoBufSerializer.encodeToByteArray<ColorDto>(data.fromDomain())
            )

            is Interaction.Drawing -> sendPayload(
                endpointId,
                ProtoBufSerializer.encodeToByteArray<InteractionDto.DrawingDto>(data.fromDomain())
            )

            is Interaction.Text -> sendPayload(
                endpointId,
                ProtoBufSerializer.encodeToByteArray<InteractionDto.TextDto>(data.fromDomain())
            )

            is Point -> sendPayload(
                endpointId,
                ProtoBufSerializer.encodeToByteArray<PointDto>(data.fromDomain())
            )

            is Size -> sendPayload(
                endpointId,
                ProtoBufSerializer.encodeToByteArray<SizeDto>(data.fromDomain())
            )

            is GameState -> sendPayload(
                endpointId,
                ProtoBufSerializer.encodeToByteArray<GameStateDto>(data.fromDomain())
            )

            is GameHomeState -> sendPayload(
                endpointId,
                ProtoBufSerializer.encodeToByteArray<GameHomeStateDto>(data.fromDomain())
            )
        }
    }

    override fun send(endpointId: String, data: PayloadData) {
        connectionsClient.sendPayloadData(endpointId, data)
    }
}
