package g.sig.questweaver.data.entities

import kotlinx.serialization.Serializable
import g.sig.questweaver.domain.entities.FileMetadata as FileMetadataDomain

@Serializable
data class FileMetadata(val name: String, val extension: String) : DataEntity {
    fun toDomain() = FileMetadataDomain(name, extension)

    companion object {
        fun FileMetadataDomain.fromDomain() = FileMetadata(name, extension)
    }
}
