package g.sig.questweaver.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class FileMetadataDto(
    val name: String,
    val extension: String,
) : Dto
