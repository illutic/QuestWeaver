package g.sig.common.data

import android.content.Context
import android.net.Uri
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

inline fun Uri.openCatching(
    context: Context,
    block: (InputStream) -> Unit,
    onIOException: (IOException) -> Unit = {},
    finally: () -> Unit = {}
) {
    try {
        context.contentResolver.openInputStream(this)?.use(block)
    } catch (e: IOException) {
        onIOException(e)
    } finally {
        finally()
    }
}

suspend inline fun Uri.copyToCacheAndDelete(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    context: Context,
    filename: String,
    onIOException: (IOException) -> Unit = {}
) {
    openCatching(
        context = context,
        block = { inputStream ->
            inputStream.copyStream(dispatcher, FileOutputStream(context.cacheDir.resolve(filename)))
        },
        onIOException = onIOException,
        finally = { context.contentResolver.delete(this, null, null) }
    )
}
