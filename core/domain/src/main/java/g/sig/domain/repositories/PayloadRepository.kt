package g.sig.domain.repositories

import g.sig.domain.entities.PayloadData
import kotlinx.coroutines.flow.Flow

interface PayloadRepository {
    val data: Flow<PayloadData>

    fun send(endpointId: String, data: PayloadData)
}