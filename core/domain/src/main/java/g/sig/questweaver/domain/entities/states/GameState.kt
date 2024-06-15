package g.sig.questweaver.domain.entities.states

import g.sig.questweaver.domain.entities.DomainEntity
import g.sig.questweaver.domain.entities.common.Game
import g.sig.questweaver.domain.entities.common.User

data class GameState(
    val game: Game,
    val connectedUsers: List<User>,
    val gameHomeState: GameHomeState,
) : DomainEntity
