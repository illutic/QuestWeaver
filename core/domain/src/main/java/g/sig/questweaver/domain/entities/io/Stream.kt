package g.sig.questweaver.domain.entities.io

import java.io.Closeable
import java.io.InputStream

data class Stream(val inputStream: InputStream) : PayloadData, Closeable {
    override fun close() {
        inputStream.close()
    }
}
