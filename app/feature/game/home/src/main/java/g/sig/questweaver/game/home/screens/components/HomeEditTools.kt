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
import g.sig.questweaver.game.home.state.AnnotationMode
import g.sig.questweaver.ui.AppIcons
import g.sig.questweaver.ui.AppTheme
import g.sig.questweaver.ui.smallSize

@Composable
fun AnnotationTools(
    annotationMode: AnnotationMode,
    isDM: Boolean,
    modifier: Modifier = Modifier,
    allowEditing: Boolean = true,
    onAnnotationModeChange: (AnnotationMode) -> Unit,
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
    annotationMode: AnnotationMode,
    allowEditing: Boolean,
    onAnnotationModeChange: (AnnotationMode) -> Unit,
) {
    HomeButton(
        onClick = {
            if (annotationMode == AnnotationMode.RemoveMode) {
                onAnnotationModeChange(AnnotationMode.Idle)
            } else {
                onAnnotationModeChange(AnnotationMode.RemoveMode)
            }
        },
        isSelected = annotationMode == AnnotationMode.RemoveMode,
        isEnabled = allowEditing,
        useTonal = true,
    ) {
        Icon(painter = AppIcons.Bin, contentDescription = "")
    }
}

@Composable
private fun AnnotationButton(
    annotationMode: AnnotationMode,
    allowEditing: Boolean,
    onAnnotationModeChange: (AnnotationMode) -> Unit,
) {
    HomeButton(
        onClick = {
            if (annotationMode == AnnotationMode.DrawingMode) {
                onAnnotationModeChange(AnnotationMode.Idle)
            } else {
                onAnnotationModeChange(AnnotationMode.DrawingMode)
            }
        },
        isSelected = annotationMode == AnnotationMode.DrawingMode,
        isEnabled = allowEditing,
    ) {
        Icon(painter = AppIcons.Edit, contentDescription = "")
    }
}

@Composable
private fun TextButton(
    annotationMode: AnnotationMode,
    allowEditing: Boolean,
    onAnnotationModeChange: (AnnotationMode) -> Unit,
) {
    HomeButton(
        onClick = {
            if (annotationMode == AnnotationMode.TextMode) {
                onAnnotationModeChange(AnnotationMode.Idle)
            } else {
                onAnnotationModeChange(AnnotationMode.TextMode)
            }
        },
        isSelected = annotationMode == AnnotationMode.TextMode,
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
    annotationMode: AnnotationMode,
    isDM: Boolean,
    onAnnotationModeChange: (AnnotationMode) -> Unit,
) {
    if (!isDM) return

    HomeButton(
        onClick = {
            if (annotationMode == AnnotationMode.DMMode) {
                onAnnotationModeChange(AnnotationMode.Idle)
            } else {
                onAnnotationModeChange(AnnotationMode.DMMode)
            }
        },
        isSelected = annotationMode == AnnotationMode.DMMode,
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
            annotationMode = AnnotationMode.Idle,
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
            annotationMode = AnnotationMode.Idle,
            isDM = true,
            onAnnotationModeChange = {},
        )
    }
}
