package g.sig.questweaver.common.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import g.sig.questweaver.common.ui.R
import g.sig.questweaver.ui.AppIcons

@Composable
fun PermissionsAlert(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Alert(
        modifier = modifier,
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
        },
    )
}
