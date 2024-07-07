package g.sig.questweaver.game.home.screens.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun GameHomeTopBar(
    title: String,
    modifier: Modifier = Modifier,
    icon: Painter? = null,
    onBackPressed: () -> Unit = {},
) {
    TopAppBar(
        title = { Text(text = title) },
        modifier = modifier,
        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
        navigationIcon = {
            icon?.let {
                IconButton(onClick = onBackPressed) {
                    Icon(painter = it, contentDescription = "Back")
                }
            }
        },
    )
}
