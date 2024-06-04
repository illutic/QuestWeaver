package g.sig.questweaver.navigation

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource

fun Context.launchInBrowser(url: String) {
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        Log.e("launchInBrowser", e.message.toString())
    }
}

val NavIcon.painter
    @Composable get() = when (this) {
        is NavIcon.DrawableNavIcon -> painterResource(id = resId)
        is NavIcon.VectorNavIcon -> rememberVectorPainter(image = vector)
    }
