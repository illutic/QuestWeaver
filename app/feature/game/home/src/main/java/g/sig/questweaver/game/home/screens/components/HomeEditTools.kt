package g.sig.questweaver.game.home.screens.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
    onAnnotationModeChange: (GameHomeState.AnnotationMode) -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = if (isDM) Arrangement.SpaceBetween else Arrangement.End,
    ) {
        DMToolsButton(annotationMode, isDM, onAnnotationModeChange)

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(smallSize),
        ) {
            RemoveButton(annotationMode, allowEditing, onAnnotationModeChange)

            AnnotationButton(annotationMode, allowEditing, onAnnotationModeChange)

            TextButton(annotationMode, allowEditing, onAnnotationModeChange)
        }
    }
}

@Composable
private fun RemoveButton(
    annotationMode: GameHomeState.AnnotationMode,
    allowEditing: Boolean,
    onAnnotationModeChange: (GameHomeState.AnnotationMode) -> Unit,
) {
    HomeButton(
        onClick = {
            if (annotationMode == GameHomeState.AnnotationMode.RemoveMode) {
                onAnnotationModeChange(GameHomeState.AnnotationMode.Idle)
            } else {
                onAnnotationModeChange(GameHomeState.AnnotationMode.RemoveMode)
            }
        },
        isSelected = annotationMode == GameHomeState.AnnotationMode.RemoveMode,
        isEnabled = allowEditing,
        useTonal = true,
    ) {
        Icon(painter = AppIcons.Bin, contentDescription = "")
    }
}

@Composable
private fun AnnotationButton(
    annotationMode: GameHomeState.AnnotationMode,
    allowEditing: Boolean,
    onAnnotationModeChange: (GameHomeState.AnnotationMode) -> Unit,
) {
    HomeButton(
        onClick = {
            if (annotationMode == GameHomeState.AnnotationMode.DrawingMode) {
                onAnnotationModeChange(GameHomeState.AnnotationMode.Idle)
            } else {
                onAnnotationModeChange(GameHomeState.AnnotationMode.DrawingMode)
            }
        },
        isSelected = annotationMode == GameHomeState.AnnotationMode.DrawingMode,
        isEnabled = allowEditing,
    ) {
        Icon(painter = AppIcons.Edit, contentDescription = "")
    }
}

@Composable
private fun TextButton(
    annotationMode: GameHomeState.AnnotationMode,
    allowEditing: Boolean,
    onAnnotationModeChange: (GameHomeState.AnnotationMode) -> Unit,
) {
    HomeButton(
        onClick = {
            if (annotationMode == GameHomeState.AnnotationMode.TextMode) {
                onAnnotationModeChange(GameHomeState.AnnotationMode.Idle)
            } else {
                onAnnotationModeChange(GameHomeState.AnnotationMode.TextMode)
            }
        },
        isSelected = annotationMode == GameHomeState.AnnotationMode.TextMode,
        isEnabled = allowEditing,
    ) {
        Text(
            "T",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
private fun DMToolsButton(
    annotationMode: GameHomeState.AnnotationMode,
    isDM: Boolean,
    onAnnotationModeChange: (GameHomeState.AnnotationMode) -> Unit,
) {
    if (!isDM) return

    HomeButton(
        onClick = {
            if (annotationMode == GameHomeState.AnnotationMode.DMMode) {
                onAnnotationModeChange(GameHomeState.AnnotationMode.Idle)
            } else {
                onAnnotationModeChange(GameHomeState.AnnotationMode.DMMode)
            }
        },
        isSelected = annotationMode == GameHomeState.AnnotationMode.DMMode,
        isLong = true,
    ) {
        Text(text = stringResource(R.string.dm_tools))
    }
}

@Preview
@Composable
fun AnnotationToolsPreview() {
    AppTheme {
        AnnotationTools(
            annotationMode = GameHomeState.AnnotationMode.Idle,
            isDM = false,
            onAnnotationModeChange = {},
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
            onAnnotationModeChange = {},
        )
    }
}
