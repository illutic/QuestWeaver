package g.sig.navigation

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.vector.ImageVector

sealed interface NavIcon {
    @JvmInline
    value class DrawableNavIcon(@DrawableRes val resId: Int) : NavIcon

    @JvmInline
    value class VectorNavIcon(val vector: ImageVector) : NavIcon
}