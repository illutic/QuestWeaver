package g.sig.questweaver.common.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.window.core.layout.WindowWidthSizeClass
import g.sig.questweaver.ui.AppTheme
import g.sig.questweaver.ui.MediumRoundedShape

@Composable
inline fun AppOutlinedTextField(
    value: String,
    crossinline onValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.labelMedium,
    label: String? = null,
    placeholder: String? = null,
    error: String? = null,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    val windowClassSize = currentWindowAdaptiveInfo().windowSizeClass
    val adaptiveModifier =
        if (windowClassSize.windowWidthSizeClass == WindowWidthSizeClass.COMPACT) {
            Modifier.fillMaxWidth()
        } else {
            Modifier
        }

    OutlinedTextField(
        modifier = modifier.then(adaptiveModifier),
        value = value,
        isError = error != null,
        onValueChange = { onValueChanged(it) },
        textStyle = style,
        label = label?.let { { Text(text = label) } },
        placeholder = placeholder?.let { { Text(text = placeholder) } },
        supportingText = {
            error?.let { error ->
                Text(
                    text = error,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        keyboardActions = keyboardActions,
        keyboardOptions = keyboardOptions,
        singleLine = true,
        shape = MediumRoundedShape
    )
}

@Composable
@Preview
fun AppOutlinedTextFieldPreview() {
    AppTheme {
        AppOutlinedTextField(
            value = "Hello",
            onValueChanged = {},
            label = "Label",
            placeholder = "Placeholder"
        )
    }
}
