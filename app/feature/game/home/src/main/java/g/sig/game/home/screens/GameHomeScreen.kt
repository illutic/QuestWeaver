package g.sig.game.home.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import g.sig.game.home.data.GameHomeViewModel

@Composable
internal fun GameHomeRoute() {
    val viewModel = hiltViewModel<GameHomeViewModel>()
    GameHomeScreen()
}

@Composable
internal fun GameHomeScreen() {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = "Home")
    }
}