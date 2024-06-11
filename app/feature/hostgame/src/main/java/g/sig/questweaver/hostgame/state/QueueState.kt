package g.sig.questweaver.hostgame.state

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import g.sig.questweaver.domain.entities.common.Device

@Stable
class QueueState {

    var advertising by mutableStateOf(false)

    val devicesToConnect = mutableStateListOf<Device>()
}
