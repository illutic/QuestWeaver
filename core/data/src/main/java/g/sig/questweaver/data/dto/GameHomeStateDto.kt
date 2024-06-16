package g.sig.questweaver.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class GameHomeStateDto(
    val annotationDtos: List<AnnotationDto>,
    val allowEditing: Boolean,
) : Dto {
    companion object {
        val Empty = GameHomeStateDto(
            annotationDtos = emptyList(),
            allowEditing = true,
        )
    }
}
