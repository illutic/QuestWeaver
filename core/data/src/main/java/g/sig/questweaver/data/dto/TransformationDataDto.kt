package g.sig.questweaver.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class TransformationDataDto(
    val scale: Float,
    val anchor: PointDto,
    val rotation: Float,
) : Dto
