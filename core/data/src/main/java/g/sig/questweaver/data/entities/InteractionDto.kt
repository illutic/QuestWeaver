@file:UseSerializers(UUIDSerializer::class)

package g.sig.questweaver.data.entities

import g.sig.questweaver.common.data.serializers.UUIDSerializer
import g.sig.questweaver.data.entities.blocks.ColorDto
import g.sig.questweaver.data.entities.blocks.ColorDto.Companion.fromDomain
import g.sig.questweaver.data.entities.blocks.PointDto
import g.sig.questweaver.data.entities.blocks.PointDto.Companion.fromDomain
import g.sig.questweaver.data.entities.blocks.SizeDto
import g.sig.questweaver.data.entities.blocks.SizeDto.Companion.fromDomain
import g.sig.questweaver.domain.entities.Interaction
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.util.UUID

@Serializable
sealed interface InteractionDto : Dto {
    fun toDomain(): Interaction

    @Serializable
    data class DrawingDto(
        val id: UUID,
        val strokeWidth: Int,
        val colorDTO: ColorDto,
        val path: List<PointDto>
    ) : InteractionDto {
        override fun toDomain() = Interaction.Drawing(
            id = id,
            strokeWidth = strokeWidth,
            color = colorDTO.toDomain(),
            path = path.map { it.toDomain() }
        )

        companion object {
            fun Interaction.Drawing.fromDomain() =
                DrawingDto(
                    id = id,
                    strokeWidth = strokeWidth,
                    colorDTO = color.fromDomain(),
                    path = path.map { it.fromDomain() }
                )
        }
    }

    @Serializable
    data class TextDto(
        val id: UUID,
        val text: String,
        val sizeDTO: SizeDto,
        val colorDTO: ColorDto,
        val anchor: PointDto
    ) : InteractionDto {
        override fun toDomain() = Interaction.Text(
            id = id,
            text = text,
            size = sizeDTO.toDomain(),
            color = colorDTO.toDomain(),
            anchor = anchor.toDomain()
        )

        companion object {
            fun Interaction.Text.fromDomain() = TextDto(
                id = id,
                text = text,
                sizeDTO = size.fromDomain(),
                colorDTO = color.fromDomain(),
                anchor = anchor.fromDomain()
            )
        }
    }

    companion object {
        fun Interaction.fromDomain(): InteractionDto = with(this@Companion) {
            when (this@fromDomain) {
                is Interaction.Drawing -> fromDomain()
                is Interaction.Text -> fromDomain()
            }
        }
    }
}
