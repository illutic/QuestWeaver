package g.sig.questweaver.game.home.screens.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import g.sig.questweaver.game.home.R
import g.sig.questweaver.game.home.state.GameHomeState
import g.sig.questweaver.ui.AppIcons
import g.sig.questweaver.ui.AppTheme
import g.sig.questweaver.ui.smallSize

@Composable
fun AnnotationTools(
    annotationMode: GameHomeState.AnnotationMode,
    isDM: Boolean,
    modifier: Modifier = Modifier,
    allowEditing: Boolean = true,
    onAnnotationModeChanged: (GameHomeState.AnnotationMode) -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = if (isDM) Arrangement.SpaceBetween else Arrangement.End,
    ) {
        if (isDM) {
            AnnotationButton(
                onClick = {
                    if (annotationMode == GameHomeState.AnnotationMode.DMMode) {
                        onAnnotationModeChanged(GameHomeState.AnnotationMode.Idle)
                    } else {
                        onAnnotationModeChanged(GameHomeState.AnnotationMode.DMMode)
                    }
                },
                isSelected = annotationMode == GameHomeState.AnnotationMode.DMMode,
                isLong = true,
            ) {
                Text(text = stringResource(R.string.dm_tools))
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(smallSize)
        ) {
            AnnotationButton(
                onClick = {
                    if (annotationMode == GameHomeState.AnnotationMode.RemoveMode) {
                        onAnnotationModeChanged(GameHomeState.AnnotationMode.Idle)
                    } else {
                        onAnnotationModeChanged(GameHomeState.AnnotationMode.RemoveMode)
                    }
                },
                isSelected = annotationMode == GameHomeState.AnnotationMode.RemoveMode,
                isEnabled = allowEditing,
                useTonal = true
            ) {
                Icon(painter = AppIcons.Bin, contentDescription = "")
            }

            AnnotationButton(
                onClick = {
                    if (annotationMode == GameHomeState.AnnotationMode.DrawingMode) {
                        onAnnotationModeChanged(GameHomeState.AnnotationMode.Idle)
                    } else {
                        onAnnotationModeChanged(GameHomeState.AnnotationMode.DrawingMode)
                    }
                },
                isSelected = annotationMode == GameHomeState.AnnotationMode.DrawingMode,
                isEnabled = allowEditing,
            ) {
                Icon(painter = AppIcons.Edit, contentDescription = "")
            }

            AnnotationButton(
                onClick = {
                    if (annotationMode == GameHomeState.AnnotationMode.TextMode) {
                        onAnnotationModeChanged(GameHomeState.AnnotationMode.Idle)
                    } else {
                        onAnnotationModeChanged(GameHomeState.AnnotationMode.TextMode)
                    }
                },
                isSelected = annotationMode == GameHomeState.AnnotationMode.TextMode,
                isEnabled = allowEditing,
            ) {
                Text(
                    "T",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

        }
    }
}

@Composable
fun AnnotationButton(
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
                enabled = isEnabled
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
fun AnnotationToolsPreview() {
    AppTheme {
        AnnotationTools(
            annotationMode = GameHomeState.AnnotationMode.Idle,
            isDM = false,
            onAnnotationModeChanged = {},
        )
    }
}

@Preview
@Composable
fun AnnotationToolsDMPreview() {
    AppTheme {
        AnnotationTools(
            annotationMode = GameHomeState.AnnotationMode.Idle,
            isDM = true,
            onAnnotationModeChanged = {},
        )
    }
}

@Preview
@Composable
fun AnnotationButtonPreview() {
    AppTheme {
        AnnotationButton(
            onClick = {},
            content = {
                Icon(painter = AppIcons.Edit, contentDescription = "")
            },
        )
    }
}

@Preview
@Composable
fun AnnotationButtonTextPreview() {
    AppTheme {
        AnnotationButton(
            onClick = {},
            isSelected = true,
            content = {
                Text(
                    "T",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            },
        )
    }
}

@Preview
@Composable
fun AnnotationLongButtonPreview() {
    AppTheme {
        AnnotationButton(
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
fun AnnotationLongButtonTextPreview() {
    AppTheme {
        AnnotationButton(
            onClick = {},
            isLong = true,
            isSelected = true,
            content = {
                Text("DM Tools")
            },
        )
    }
}
