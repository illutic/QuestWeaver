package g.sig.questweaver.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalInspectionMode

@Composable
fun AppTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        isSystemInDarkTheme() -> darkScheme
        else -> lightScheme
    }
    val isInEditMode = LocalInspectionMode.current

    MaterialTheme(
        colorScheme = colorScheme,
        typography = if (isInEditMode) MaterialTheme.typography else appTypography
    ) {
        content()
    }
}
