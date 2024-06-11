package g.sig.questweaver.data.entities.io

import g.sig.questweaver.data.entities.Dto
import g.sig.questweaver.domain.entities.io.FileMetadata
import kotlinx.serialization.Serializable

@Serializable
data class FileMetadataDto(val name: String, val extension: String) : Dto {
    fun toDomain() = FileMetadata(name, extension)

    companion object {
        fun FileMetadata.fromDomain() = FileMetadataDto(name, extension)
    }
}
