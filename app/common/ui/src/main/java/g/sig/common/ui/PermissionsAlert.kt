package g.sig.common.ui

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import g.sig.ui.AppIcons
import g.sig.ui.components.Alert

@Composable
fun PermissionsAlert(onClick: () -> Unit) {
    Alert(
        primaryColor = MaterialTheme.colorScheme.error,
        onClick = onClick,
        trailingContent = {
            Icon(painter = AppIcons.ChevronRight, contentDescription = "")
        },
        leadingContent = {
            Icon(painter = AppIcons.Info, contentDescription = "")
        },
        content = {
            Text(text = stringResource(id = R.string.permission_alert))
        }
    )
}