package g.sig.questweaver.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class GameStateDto(
    val gameId: String,
    val connectedUsers: List<UserDto> = emptyList(),
    val annotationDtos: List<AnnotationDto> = emptyList(),
    val allowEditing: Boolean = true,
) : Dto
