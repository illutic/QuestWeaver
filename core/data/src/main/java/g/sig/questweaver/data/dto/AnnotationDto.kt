@file:UseSerializers(UUIDSerializer::class)

package g.sig.questweaver.data.dto

import g.sig.questweaver.common.data.serializers.UUIDSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers

@Serializable
sealed interface AnnotationDto : Dto {
    @Serializable
    data class DrawingDto(
        val id: String,
        val createdBy: String,
        val strokeSize: SizeDto,
        val colorDto: ColorDto,
        val path: List<PointDto>
    ) : AnnotationDto

    @Serializable
    data class TextDto(
        val id: String,
        val createdBy: String,
        val text: String,
        val sizeDTO: SizeDto,
        val colorDTO: ColorDto,
        val anchor: PointDto
    ) : AnnotationDto

    @Serializable
    data class ImageDto(
        val id: String,
        val createdBy: String,
        val size: SizeDto,
        val anchor: PointDto,
        val fileDto: FileDto
    ) : AnnotationDto
}
