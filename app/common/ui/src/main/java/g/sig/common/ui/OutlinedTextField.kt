package g.sig.common.ui

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisallowComposableCalls
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import g.sig.ui.LargeRoundedShape

@Composable
inline fun AppOutlinedTextField(
    modifier: Modifier = Modifier,
    value: String,
    crossinline onValueChanged: (String) -> Unit,
    style: TextStyle = MaterialTheme.typography.labelMedium,
    label: String? = null,
    placeholder: String? = null,
    error: String? = null,
    isLastField: Boolean = false,
    crossinline onNextField: @DisallowComposableCalls () -> Unit = {},
    crossinline onDone: @DisallowComposableCalls () -> Unit = {}
) {
    val keyboardActions = remember {
        KeyboardActions(
            onDone = if (isLastField) {
                {
                    KeyboardActions.Default.onDone?.invoke(this)
                    onDone()
                }
            } else null,
            onNext = if (!isLastField) {
                {
                    KeyboardActions.Default.onNext?.invoke(this)
                    onNextField()
                }
            } else null
        )
    }

    OutlinedTextField(
        modifier = modifier,
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
        singleLine = true,
        shape = LargeRoundedShape
    )
}