package g.sig.questweaver.game.home.screens.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import g.sig.questweaver.game.home.R
import g.sig.questweaver.game.home.state.GameHomeIntent
import g.sig.questweaver.game.home.state.GameHomeState
import g.sig.questweaver.ui.largeSize

@Suppress("TopLevelPropertyNaming")
private const val SLIDER_WIDTH_PERCENT = 0.8f

@Suppress("TopLevelPropertyNaming")
private const val LABEL_WIDTH_PERCENT = 0.2f

@Composable
fun HomeEditControls(
    state: GameHomeState,
    postIntent: (GameHomeIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.align(Alignment.End),
            horizontalArrangement = Arrangement.spacedBy(largeSize),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ColorButton(
                color = state.selectedColor,
                onClick = { postIntent(GameHomeIntent.ShowColorPicker) },
            )
        }

        HomeSlider(
            label = stringResource(id = R.string.size),
            value = state.selectedSize.width,
            onValueChanged = { postIntent(GameHomeIntent.SelectSize(it)) },
        )
        HomeSlider(
            label = stringResource(id = R.string.opacity),
            value = state.opacity,
            onValueChanged = { postIntent(GameHomeIntent.SelectOpacity(it)) },
        )
    }
}

@Composable
private fun HomeSlider(
    label: String,
    value: Float,
    onValueChanged: (Float) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            modifier = Modifier.weight(LABEL_WIDTH_PERCENT),
            text = label,
            style = MaterialTheme.typography.labelLarge,
        )
        Slider(
            modifier = Modifier.weight(SLIDER_WIDTH_PERCENT),
            value = value,
            onValueChange = onValueChanged,
        )
    }
}
