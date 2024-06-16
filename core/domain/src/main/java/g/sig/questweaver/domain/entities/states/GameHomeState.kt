package g.sig.questweaver.domain.entities.states

import g.sig.questweaver.domain.entities.DomainEntity
import g.sig.questweaver.domain.entities.common.Annotation

data class GameHomeState(
    val annotations: List<Annotation>,
    val allowEditing: Boolean,
) : DomainEntity {

    companion object {
        val Empty = GameHomeState(
            annotations = emptyList(),
            allowEditing = false,
        )
    }
}
