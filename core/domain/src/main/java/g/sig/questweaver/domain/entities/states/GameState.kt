package g.sig.questweaver.domain.entities.states

import g.sig.questweaver.domain.entities.DomainEntity
import g.sig.questweaver.domain.entities.common.Annotation
import g.sig.questweaver.domain.entities.common.User

data class GameState(
    val gameId: String,
    val connectedUsers: List<User> = emptyList(),
    val annotations: List<Annotation> = emptyList(),
    val allowEditing: Boolean = true,
) : DomainEntity
