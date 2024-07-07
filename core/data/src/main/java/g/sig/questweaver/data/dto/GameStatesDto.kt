package g.sig.questweaver.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class GameStatesDto(
    val gameStatesDto: List<GameStateDto> = emptyList(),
) : Dto
