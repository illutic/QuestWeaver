package g.sig.questweaver.domain.repositories

import g.sig.questweaver.domain.entities.io.PayloadData
import kotlinx.coroutines.flow.Flow

interface PayloadRepository {
    val data: Flow<PayloadData>

    fun send(endpointId: String, data: PayloadData)
}
