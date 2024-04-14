package g.sig.data.datasources.recentgames

import g.sig.data.entities.Game
import kotlinx.serialization.Serializable

@Serializable
data class Games(val games: List<Game>)
