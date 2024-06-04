package g.sig.questweaver.game.ai.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import g.sig.questweaver.game.ai.data.GameAiViewModel

@Composable
internal fun GameAiRoute() {
    hiltViewModel<GameAiViewModel>()
    GameAiScreen()
}

@Composable
internal fun GameAiScreen() {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = "AI")
    }
}
