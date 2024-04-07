@file:UseSerializers(UriSerializer::class)

package g.sig.data.entities

import android.net.Uri
import g.sig.common.data.serializers.UriSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.io.Closeable
import java.io.InputStream

sealed interface DataEntity {
    @Serializable
    data class Message(val content: ByteArray) : DataEntity {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Message

            return content.contentEquals(other.content)
        }

        override fun hashCode(): Int {
            return content.contentHashCode()
        }
    }

    @Serializable
    data class File(val uri: Uri, val metadata: FileMetadata) : DataEntity

    data class Stream(val inputStream: InputStream) : DataEntity, Closeable {
        override fun close() {
            inputStream.close()
        }
    }
}