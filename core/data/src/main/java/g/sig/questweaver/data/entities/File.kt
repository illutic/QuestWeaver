@file:UseSerializers(UriSerializer::class)

package g.sig.questweaver.data.entities

import android.net.Uri
import g.sig.questweaver.common.data.serializers.UriSerializer
import g.sig.questweaver.data.utils.toDomain
import g.sig.questweaver.domain.entities.File
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers

@Serializable
data class File(val uri: Uri, val metadata: FileMetadata) : DataEntity {
    fun toDomain() = File(uri.toDomain(), metadata.toDomain())
}
