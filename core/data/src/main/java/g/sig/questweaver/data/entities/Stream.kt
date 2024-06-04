package g.sig.questweaver.data.entities

import g.sig.questweaver.domain.entities.Stream
import java.io.Closeable
import java.io.InputStream

data class Stream(val inputStream: InputStream) : DataEntity, Closeable {
    fun toDomain() = Stream(inputStream)
    override fun close() {
        inputStream.close()
    }
}
