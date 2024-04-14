package g.sig.data.entities

import g.sig.domain.entities.Stream
import java.io.Closeable
import java.io.InputStream

data class Stream(val inputStream: InputStream) : DataEntity, Closeable {
    fun toDomain() = Stream(inputStream)
    override fun close() {
        inputStream.close()
    }
}