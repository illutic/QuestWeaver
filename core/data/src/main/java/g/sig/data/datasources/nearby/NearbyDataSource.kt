package g.sig.data.datasources.nearby

import g.sig.data.entities.User
import g.sig.data.nearby.entities.ConnectionState
import kotlinx.coroutines.flow.Flow

interface NearbyDataSource {
    fun discover(user: User): Flow<ConnectionState>
    fun advertise(name: String): Flow<ConnectionState>
    fun cancelAdvertisement()
    fun requestConnection(user: User, endpointId: String): Flow<ConnectionState>
    fun acceptConnection(endpointId: String): Flow<ConnectionState>
    fun rejectConnection(endpointId: String): Flow<ConnectionState>
}