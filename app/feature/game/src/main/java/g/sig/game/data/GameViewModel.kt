package g.sig.game.data

import android.content.Context
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import g.sig.game.ai.navigation.GameAiRoute
import g.sig.game.chat.navigation.GameChatRoute
import g.sig.game.home.R
import g.sig.game.home.navigation.GameHomeRoute
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    @ApplicationContext context: Context
) : ViewModel() {
    val bottomNavRoutes = listOf(
        GameHomeRoute(label = context.getString(R.string.game_home_label)),
        GameChatRoute(label = context.getString(R.string.game_chat_label)),
        GameAiRoute(label = context.getString(R.string.game_ai_label)),
    )
}