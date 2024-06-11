package g.sig.questweaver.domain.entities.states

import g.sig.questweaver.domain.entities.common.Game
import g.sig.questweaver.domain.entities.common.User
import g.sig.questweaver.domain.entities.io.PayloadData

data class GameState(
    val game: Game,
    val connectedUsers: List<User>,
    val gameHomeState: GameHomeState,
) : PayloadData
