package g.sig.questweaver.data.dto

import kotlinx.serialization.Serializable

@Serializable
internal data class GamesDto(
    val gameDtos: List<GameDto>,
) : Dto
