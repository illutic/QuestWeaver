package g.sig.questweaver.data.entities.states

import g.sig.questweaver.data.entities.Dto
import g.sig.questweaver.data.entities.InteractionDto
import g.sig.questweaver.data.entities.InteractionDto.Companion.fromDomain
import g.sig.questweaver.domain.entities.states.GameHomeState
import kotlinx.serialization.Serializable

@Serializable
data class GameHomeStateDto(
    val interactions: List<InteractionDto>,
    val allowEditing: Boolean,
) : Dto {
    fun toDomain() = GameHomeState(
        interactions = interactions.map { it.toDomain() },
        allowEditing = allowEditing,
    )

    companion object {
        val Empty = GameHomeStateDto(
            interactions = emptyList(),
            allowEditing = true,
        )

        fun GameHomeState.fromDomain() = GameHomeStateDto(
            interactions = interactions.map { it.fromDomain() },
            allowEditing = allowEditing,
        )
    }
}
