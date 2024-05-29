package g.sig.ui

import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Warning
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
    val ChevronRight @Composable get() = MaterialIcons.AutoMirrored.Filled.KeyboardArrowRight.painter
    val PersonOutline @Composable get() = MaterialIcons.Outlined.Person.painter
    val SettingsOutline @Composable get() = MaterialIcons.Outlined.Settings.painter
    val Check @Composable get() = MaterialIcons.Filled.Check.painter
    val Close @Composable get() = MaterialIcons.Filled.Close.painter
    val ConnectingDevice @Composable get() = R.drawable.ic_connect_without_contact_24.painter
    val HomeOutlinedVector = MaterialIcons.Outlined.Home
    val HomeFilledVector = MaterialIcons.Filled.Home
    val HomeOutlined @Composable get() = HomeOutlinedVector.painter
    val HomeFilled @Composable get() = HomeFilledVector.painter
    val Warning @Composable get() = MaterialIcons.Outlined.Warning.painter
    val Refresh @Composable get() = MaterialIcons.Outlined.Refresh.painter
}

