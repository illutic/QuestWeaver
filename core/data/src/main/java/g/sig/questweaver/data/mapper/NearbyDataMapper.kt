package g.sig.questweaver.data.mapper

import g.sig.questweaver.data.entities.Dto
import g.sig.questweaver.data.entities.InteractionDto
import g.sig.questweaver.data.entities.blocks.ColorDto
import g.sig.questweaver.data.entities.blocks.PointDto
import g.sig.questweaver.data.entities.blocks.SizeDto
import g.sig.questweaver.data.entities.common.GameDto
import g.sig.questweaver.data.entities.common.UserDto
import g.sig.questweaver.data.entities.io.FileDto
import g.sig.questweaver.data.entities.io.FileMetadataDto
import g.sig.questweaver.data.entities.io.StreamDto
import g.sig.questweaver.domain.entities.io.PayloadData

fun Dto.toDomainData(): PayloadData = when (this) {
    is FileDto -> toDomain()
    is FileMetadataDto -> toDomain()
    is GameDto -> toDomain()
    is UserDto -> toDomain()
    is StreamDto -> toDomain()
    is ColorDto -> toDomain()
    is InteractionDto.DrawingDto -> toDomain()
    is InteractionDto.TextDto -> toDomain()
    is PointDto -> toDomain()
    is SizeDto -> toDomain()
    else -> throw IllegalArgumentException("Unknown Dto type: $this")
}
