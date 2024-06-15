package g.sig.questweaver.data.repositories

import com.google.android.gms.nearby.connection.ConnectionsClient
import g.sig.questweaver.data.datasources.nearby.PayloadCallback
import g.sig.questweaver.data.mapper.toDomain
import g.sig.questweaver.data.mapper.toDto
import g.sig.questweaver.data.nearby.sendPayload
import g.sig.questweaver.data.serializers.serializeDto
import g.sig.questweaver.domain.entities.DomainEntity
import g.sig.questweaver.domain.repositories.PayloadRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PayloadRepositoryImpl(
    private val connectionsClient: ConnectionsClient,
    payloadCallback: PayloadCallback
) : PayloadRepository {
    override val data: Flow<DomainEntity> = payloadCallback.data.map { it.toDomain() }

    override fun send(endpointId: String, data: DomainEntity) {
        connectionsClient.sendPayload(endpointId, serializeDto(data.toDto()))
    }
}
