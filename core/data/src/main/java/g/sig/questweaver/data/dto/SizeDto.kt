package g.sig.questweaver.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class SizeDto(
    val width: Float,
    val height: Float,
) : Dto
