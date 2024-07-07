package g.sig.questweaver.data.datasources.nearby

import g.sig.questweaver.data.dto.UserDto
import g.sig.questweaver.domain.entities.states.ConnectionState
import kotlinx.coroutines.flow.Flow

interface NearbyDataSource {
    fun discover(userDto: UserDto): Flow<ConnectionState>

    fun cancelDiscovery()

    fun advertise(name: String): Flow<ConnectionState>

    fun cancelAdvertisement()

    fun requestConnection(
        userDto: UserDto,
        endpointId: String,
    ): Flow<ConnectionState>

    fun acceptConnection(endpointId: String): Flow<ConnectionState>

    fun rejectConnection(endpointId: String): Flow<ConnectionState>
}
