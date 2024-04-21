package g.sig.game.chat.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import g.sig.game.chat.data.GameChatViewModel

@Composable
internal fun GameChatRoute() {
    val viewModel = hiltViewModel<GameChatViewModel>()
    GameChatScreen()
}

@Composable
internal fun GameChatScreen() {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = "Chat")
    }
}