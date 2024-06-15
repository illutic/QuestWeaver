package g.sig.questweaver.domain.repositories

import g.sig.questweaver.domain.entities.DomainEntity
import kotlinx.coroutines.flow.Flow

interface PayloadRepository {
    val data: Flow<DomainEntity>

    fun send(endpointId: String, data: DomainEntity)
}
