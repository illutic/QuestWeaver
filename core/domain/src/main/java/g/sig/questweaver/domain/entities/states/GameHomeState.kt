package g.sig.questweaver.domain.entities.states

import g.sig.questweaver.domain.entities.Interaction
import g.sig.questweaver.domain.entities.io.PayloadData

data class GameHomeState(
    val interactions: List<Interaction>,
    val allowEditing: Boolean,
) : PayloadData {

    companion object {
        val Empty = GameHomeState(
            interactions = emptyList(),
            allowEditing = false,
        )
    }
}
