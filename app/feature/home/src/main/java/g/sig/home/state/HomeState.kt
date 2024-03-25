package g.sig.home.state

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import g.sig.domain.entities.RecentGame

@Stable
internal class HomeState {
    var userName: String by mutableStateOf("")
    var hasPermissions: Boolean by mutableStateOf(false)
    var recentGames: List<RecentGame> by mutableStateOf(emptyList())
}