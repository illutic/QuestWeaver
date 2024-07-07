@file:UseSerializers(UriSerializer::class)

package g.sig.questweaver.data.dto

import android.net.Uri
import g.sig.questweaver.common.data.serializers.UriSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers

@Serializable
data class FileDto(
    val uri: Uri,
    val metadata: FileMetadataDto,
) : Dto
