@file:UseSerializers(UriSerializer::class)

package g.sig.questweaver.data.entities.io

import android.net.Uri
import g.sig.questweaver.common.data.serializers.UriSerializer
import g.sig.questweaver.data.entities.Dto
import g.sig.questweaver.data.utils.toDomain
import g.sig.questweaver.domain.entities.io.File
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers

@Serializable
data class FileDto(val uri: Uri, val metadata: FileMetadataDto) : Dto {
    fun toDomain() = File(uri.toDomain(), metadata.toDomain())
}
