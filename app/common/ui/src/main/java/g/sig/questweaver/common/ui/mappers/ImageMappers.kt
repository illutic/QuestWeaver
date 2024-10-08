package g.sig.questweaver.common.ui.mappers

import android.content.Context
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.net.toUri
import g.sig.questweaver.domain.entities.common.Annotation

fun Annotation.Image.load(context: Context): ImageBitmap =
    context.contentResolver.openInputStream(uri.value.toUri())?.use {
        BitmapFactory
            .decodeStream(it)
            .asImageBitmap()
    } ?: error("Failed to load image from uri: $uri")
