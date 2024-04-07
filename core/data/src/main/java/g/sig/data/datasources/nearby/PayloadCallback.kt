package g.sig.data.datasources.nearby

import g.sig.data.entities.DataEntity
import kotlinx.coroutines.flow.Flow
import com.google.android.gms.nearby.connection.PayloadCallback as NearbyPayloadCallback

abstract class PayloadCallback : NearbyPayloadCallback() {
    abstract val data: Flow<DataEntity>
}