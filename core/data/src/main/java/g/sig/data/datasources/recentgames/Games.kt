package g.sig.data.datasources.recentgames

import g.sig.data.entities.Game
import kotlinx.serialization.Serializable

@Serializable
internal data class Games(val games: List<Game>)
