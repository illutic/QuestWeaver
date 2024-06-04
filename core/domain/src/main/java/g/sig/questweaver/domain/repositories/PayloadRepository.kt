package g.sig.questweaver.domain.repositories

import g.sig.questweaver.domain.entities.PayloadData
import kotlinx.coroutines.flow.Flow

interface PayloadRepository {
    val data: Flow<PayloadData>

    fun send(endpointId: String, data: PayloadData)
}
