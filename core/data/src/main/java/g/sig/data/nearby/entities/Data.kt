@file:UseSerializers(UriSerializer::class)

package g.sig.data.nearby.entities

import android.net.Uri
import g.sig.data.nearby.serializers.UriSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers

@Serializable
sealed interface Data {
    @Serializable
    data class File(val name: String, val size: Long, val uri: Uri) : Data

    @Serializable
    data class Stream(val uri: Uri) : Data

    @Serializable
    sealed interface Message : Data
}