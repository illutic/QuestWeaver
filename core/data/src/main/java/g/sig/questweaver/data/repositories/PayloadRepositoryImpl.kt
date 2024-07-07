package g.sig.questweaver.data.repositories

import com.google.android.gms.nearby.connection.ConnectionsClient
import g.sig.questweaver.data.datasources.nearby.PayloadCallback
import g.sig.questweaver.data.dto.PayloadDataDto
import g.sig.questweaver.data.mapper.toDomain
import g.sig.questweaver.data.mapper.toDto
import g.sig.questweaver.data.nearby.sendPayload
import g.sig.questweaver.data.serializers.serializePayloadData
import g.sig.questweaver.domain.entities.IncomingPayload
import g.sig.questweaver.domain.entities.PayloadData
import g.sig.questweaver.domain.repositories.PayloadRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PayloadRepositoryImpl(
    private val connectionsClient: ConnectionsClient,
    payloadCallback: PayloadCallback,
) : PayloadRepository {
    override val data: Flow<IncomingPayload> =
        payloadCallback.data.map {
            val payloadData =
                when (it.payloadData) {
                    is PayloadDataDto.Broadcast ->
                        PayloadData.Broadcast(
                            data = it.payloadData.data.toDomain(),
                        )

                    is PayloadDataDto.Unicast ->
                        PayloadData.Unicast(
                            data = it.payloadData.data.toDomain(),
                            destination = it.payloadData.destination,
                        )
                }

            IncomingPayload(origin = it.origin, payloadData = payloadData)
        }

    override fun send(
        endpointId: String,
        data: PayloadData,
    ) {
        val payloadDataDto =
            when (data) {
                is PayloadData.Broadcast -> PayloadDataDto.Broadcast(data = data.data.toDto())

                is PayloadData.Unicast ->
                    PayloadDataDto.Unicast(
                        data = data.data.toDto(),
                        destination = data.destination,
                    )
            }

        connectionsClient.sendPayload(endpointId, serializePayloadData(payloadDataDto))
    }
}
