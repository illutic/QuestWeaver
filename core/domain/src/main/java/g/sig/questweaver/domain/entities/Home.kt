package g.sig.questweaver.domain.entities

import g.sig.questweaver.domain.entities.common.Game
import g.sig.questweaver.domain.entities.common.User

data class Home(val user: User, val recentGames: List<Game>, val permissions: List<Permission>)
