package g.sig.questweaver.hostgame.screens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import g.sig.questweaver.hostgame.R
import g.sig.questweaver.ui.AppIcons

@Composable
internal fun ConnectionDialog(
    onDismissRequest: () -> Unit,
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
) {
    AlertDialog(
        icon = {
            Icon(
                painter = AppIcons.ConnectingDevice,
                contentDescription = null,
            )
        },
        onDismissRequest = onDismissRequest,
        title = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.titleLarge,
                text = stringResource(R.string.host_game_dialog_title),
            )
        },
        text = {
            Text(text = stringResource(R.string.host_game_dialog_message))
        },
        dismissButton = {
            TextButton(onClick = {
                onCancel()
                onDismissRequest()
            }) {
                Text(text = stringResource(R.string.host_game_dialog_no))
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onConfirm()
                onDismissRequest()
            }) {
                Text(text = stringResource(R.string.host_game_dialog_yes))
            }
        },
    )
}
