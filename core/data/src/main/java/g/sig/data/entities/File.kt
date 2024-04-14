@file:UseSerializers(UriSerializer::class)

package g.sig.data.entities

import android.net.Uri
import g.sig.common.data.serializers.UriSerializer
import g.sig.data.utils.toDomain
import g.sig.domain.entities.File
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers

@Serializable
data class File(val uri: Uri, val metadata: FileMetadata) : DataEntity {
    fun toDomain() = File(uri.toDomain(), metadata.toDomain())
}