package g.sig.questweaver.data.entities.io

import g.sig.questweaver.data.entities.Dto
import g.sig.questweaver.domain.entities.io.Stream
import java.io.Closeable
import java.io.InputStream

data class StreamDto(val inputStream: InputStream) : Dto, Closeable {
    fun toDomain() = Stream(inputStream)
    override fun close() {
        inputStream.close()
    }
}
