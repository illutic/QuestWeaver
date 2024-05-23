package g.sig.game.data

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import g.sig.game.ai.navigation.GameAiRoute
import g.sig.game.chat.navigation.GameChatRoute
import g.sig.game.home.R
import g.sig.game.home.navigation.GameHomeRoute
import g.sig.game.state.GameIntent
import g.sig.navigation.DecoratedRoute
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    @ApplicationContext context: Context
) : ViewModel() {
    var selectedRoute: DecoratedRoute by mutableStateOf(GameHomeRoute(label = context.getString(R.string.game_home_label)))
    val gameRoutes = listOf(
        GameHomeRoute(label = context.getString(R.string.game_home_label)),
        GameChatRoute(label = context.getString(R.string.game_chat_label)),
        GameAiRoute(label = context.getString(R.string.game_ai_label)),
    )

    fun handleIntent(intent: GameIntent) {
        when (intent) {
            is GameIntent.SelectRoute -> gameRoutes.find { it == intent.route }?.let { selectedRoute = it }
        }
    }
}