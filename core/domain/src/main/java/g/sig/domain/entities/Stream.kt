package g.sig.domain.entities

import java.io.Closeable
import java.io.InputStream

data class Stream(val inputStream: InputStream) : PayloadData, Closeable {
    override fun close() {
        inputStream.close()
    }
}