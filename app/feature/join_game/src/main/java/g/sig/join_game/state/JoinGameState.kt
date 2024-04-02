package g.sig.join_game.state

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import g.sig.domain.entities.Game

@Stable
class JoinGameState internal constructor() {
    internal var games = mutableStateListOf<Game>()
    internal var hasPermissions by mutableStateOf(false)
    internal var isLoading by mutableStateOf(false)
}