package g.sig.questweaver.hostgame.state

import androidx.annotation.StringRes
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

@Stable
class HostGameState {
    var gameName by mutableStateOf("")

    @delegate:StringRes
    var gameNameError by mutableStateOf<Int?>(null)

    var description by mutableStateOf("")

    @delegate:StringRes
    var descriptionError by mutableStateOf<Int?>(null)

    var playerCount by mutableIntStateOf(4)

    @delegate:StringRes
    var playerCountError by mutableStateOf<Int?>(null)

    var hasPermissions by mutableStateOf(false)

    var showConnectionDialog by mutableStateOf(false)
}
