@file:UseSerializers(UUIDSerializer::class)

package g.sig.questweaver.data.dto

import g.sig.questweaver.common.data.serializers.UUIDSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.util.UUID

@Serializable
sealed interface InteractionDto : Dto {
    @Serializable
    data class DrawingDto(
        val id: UUID,
        val strokeWidth: Int,
        val colorDto: ColorDto,
        val path: List<PointDto>
    ) : InteractionDto

    @Serializable
    data class TextDto(
        val id: UUID,
        val text: String,
        val sizeDTO: SizeDto,
        val colorDTO: ColorDto,
        val anchor: PointDto
    ) : InteractionDto
}
