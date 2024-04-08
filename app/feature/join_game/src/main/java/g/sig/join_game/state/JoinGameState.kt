package g.sig.join_game.state

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import g.sig.domain.entities.Device

@Stable
class JoinGameState internal constructor() {
    internal var devices = mutableStateListOf<Device>()
    internal var hasPermissions by mutableStateOf(false)
    internal var discovering by mutableStateOf(false)

    internal data class ShowDeviceConfirmationDialog(val device: Device)
}