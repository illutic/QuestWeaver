package g.sig.questweaver.home.screens

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import g.sig.questweaver.domain.entities.common.Game
import g.sig.questweaver.home.R

@Composable
fun GameNotFoundDialog(
    game: Game,
    onDismiss: () -> Unit,
    onHostGame: (gameId: String) -> Unit,
    onRemoveGame: (gameId: String) -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = stringResource(R.string.game_not_found))
        },
        text = {
            Text(text = stringResource(R.string.game_not_found_message, game.title))
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onRemoveGame(game.gameId)
                    onDismiss()
                },
            ) {
                Text(text = stringResource(R.string.remove_game))
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onHostGame(game.gameId)
                    onDismiss()
                },
            ) {
                Text(text = stringResource(R.string.host_game))
            }
        },
    )
}
