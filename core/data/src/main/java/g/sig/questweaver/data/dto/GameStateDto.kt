package g.sig.questweaver.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class GameStateDto(
    val game: GameDto,
    val connectedUsers: List<UserDto>,
    val gameHomeState: GameHomeStateDto
) : Dto {
    companion object {
        val Empty = GameStateDto(GameDto.Empty, emptyList(), GameHomeStateDto.Empty)
    }
}
