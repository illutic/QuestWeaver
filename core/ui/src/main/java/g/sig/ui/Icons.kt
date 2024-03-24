package g.sig.ui

import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.material.icons.Icons as MaterialIcons

val Int.painter @Composable get() = painterResource(this)

val ImageVector.painter @Composable get() = rememberVectorPainter(this)

object AppIcons {
    val Back @Composable get() = MaterialIcons.AutoMirrored.Filled.ArrowBack.painter
    val Info @Composable get() = MaterialIcons.Filled.Info.painter
}

