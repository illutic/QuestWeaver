package g.sig.data.nearby.utils

import java.io.InputStream
import java.io.OutputStream

fun InputStream.copyStream(outputStream: OutputStream) {
    val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
    var read: Int
    while (read(buffer).also { read = it } != -1) {
        outputStream.write(buffer, 0, read)
    }
}
