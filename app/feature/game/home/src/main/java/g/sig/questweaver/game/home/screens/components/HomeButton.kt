package g.sig.questweaver.game.home.screens.components

import androidx.compose.material3.Button
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import g.sig.questweaver.ui.AppIcons
import g.sig.questweaver.ui.AppTheme

@Composable
fun HomeButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    isSelected: Boolean = false,
    isLong: Boolean = false,
    useTonal: Boolean = false,
    content: @Composable () -> Unit,
) {
    if (isLong) {
        if (isSelected) {
            Button(
                modifier = modifier,
                onClick = onClick,
                enabled = isEnabled,
            ) {
                content()
            }
        } else {
            OutlinedButton(modifier = modifier, onClick = onClick, enabled = isEnabled) {
                content()
            }
        }
    } else {
        if (isSelected) {
            FilledIconButton(modifier = modifier, onClick = onClick, enabled = isEnabled) {
                content()
            }
        } else {
            if (useTonal) {
                FilledTonalIconButton(modifier = modifier, onClick = onClick, enabled = isEnabled) {
                    content()
                }
            } else {
                OutlinedIconButton(modifier = modifier, onClick = onClick, enabled = isEnabled) {
                    content()
                }
            }
        }
    }
}

@Preview
@Composable
fun ButtonPreview() {
    AppTheme {
        HomeButton(
            onClick = {},
            content = {
                Icon(painter = AppIcons.Edit, contentDescription = "")
            },
        )
    }
}

@Preview
@Composable
fun ButtonTextPreview() {
    AppTheme {
        HomeButton(
            onClick = {},
            isSelected = true,
            content = {
                Text(
                    "T",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                )
            },
        )
    }
}

@Preview
@Composable
fun LongButtonPreview() {
    AppTheme {
        HomeButton(
            onClick = {},
            isLong = true,
            isSelected = true,
            content = {
                Icon(painter = AppIcons.Edit, contentDescription = "")
            },
        )
    }
}

@Preview
@Composable
fun LongButtonTextPreview() {
    AppTheme {
        HomeButton(
            onClick = {},
            isLong = true,
            isSelected = true,
            content = {
                Text("DM Tools")
            },
        )
    }
}
