package g.sig.questweaver.data.datasources.nearby

import g.sig.questweaver.data.entities.DataEntity
import kotlinx.coroutines.flow.Flow
import com.google.android.gms.nearby.connection.PayloadCallback as NearbyPayloadCallback

abstract class PayloadCallback : NearbyPayloadCallback() {
    abstract val data: Flow<DataEntity>
}
