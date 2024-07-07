package g.sig.questweaver.common.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.OutputStream

suspend fun InputStream.copyStream(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    outputStream: OutputStream,
) {
    val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
    var read: Int
    withContext(dispatcher) {
        while (read(buffer).also { read = it } != -1) {
            outputStream.write(buffer, 0, read)
        }
    }
}
