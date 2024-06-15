package g.sig.questweaver.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class GameHomeStateDto(
    val interactions: List<InteractionDto>,
    val allowEditing: Boolean,
) : Dto {
    companion object {
        val Empty = GameHomeStateDto(
            interactions = emptyList(),
            allowEditing = true,
        )
    }
}
