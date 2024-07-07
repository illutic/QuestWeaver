@file:UseSerializers(UriSerializer::class)

package g.sig.questweaver.data.nearby.entities

import android.net.Uri
import g.sig.questweaver.common.data.serializers.UriSerializer
import kotlinx.serialization.UseSerializers
import java.io.Closeable
import java.io.InputStream

sealed interface Data {
    data class Message(
        val byteArray: ByteArray,
    ) : Data {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Message

            return byteArray.contentEquals(other.byteArray)
        }

        override fun hashCode(): Int = byteArray.contentHashCode()

        companion object {
            val Empty = Message(ByteArray(0))
        }
    }

    data class File(
        var uri: Uri,
        val fileMetadata: Message,
    ) : Data

    data class Stream(
        val inputStream: InputStream,
    ) : Data,
        Closeable {
        override fun close() {
            inputStream.close()
        }
    }
}
