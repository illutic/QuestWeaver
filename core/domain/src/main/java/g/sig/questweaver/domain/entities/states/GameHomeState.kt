package g.sig.questweaver.domain.entities.states

import g.sig.questweaver.domain.entities.DomainEntity
import g.sig.questweaver.domain.entities.common.Interaction

data class GameHomeState(
    val interactions: List<Interaction>,
    val allowEditing: Boolean,
) : DomainEntity {

    companion object {
        val Empty = GameHomeState(
            interactions = emptyList(),
            allowEditing = false,
        )
    }
}
