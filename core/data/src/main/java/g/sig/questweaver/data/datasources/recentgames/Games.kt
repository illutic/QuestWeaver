package g.sig.questweaver.data.datasources.recentgames

import g.sig.questweaver.data.entities.Game
import kotlinx.serialization.Serializable

@Serializable
internal data class Games(val games: List<Game>)
