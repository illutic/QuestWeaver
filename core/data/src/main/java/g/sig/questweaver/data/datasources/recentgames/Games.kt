package g.sig.questweaver.data.datasources.recentgames

import g.sig.questweaver.data.entities.common.GameDto
import kotlinx.serialization.Serializable

@Serializable
internal data class Games(val gameDtos: List<GameDto>)
