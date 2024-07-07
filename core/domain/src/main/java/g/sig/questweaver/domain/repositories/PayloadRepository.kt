package g.sig.questweaver.domain.repositories

import g.sig.questweaver.domain.entities.IncomingPayload
import g.sig.questweaver.domain.entities.PayloadData
import kotlinx.coroutines.flow.Flow

interface PayloadRepository {
    val data: Flow<IncomingPayload>

    fun send(
        endpointId: String,
        data: PayloadData,
    )
}
