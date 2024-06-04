package g.sig.questweaver.game.home.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import g.sig.questweaver.game.home.R
import g.sig.questweaver.game.home.data.GameHomeViewModel
import g.sig.questweaver.game.home.screens.components.GameHomeTopBar
import g.sig.questweaver.game.home.state.GameHomeEvent
import g.sig.questweaver.game.home.state.GameHomeIntent
import g.sig.questweaver.ui.AppIcons
import g.sig.questweaver.ui.AppTheme

@Composable
internal fun GameHomeRoute(onBackPressed: () -> Unit) {
    val viewModel = hiltViewModel<GameHomeViewModel>()

    LaunchedEffect(Unit) {
        viewModel.events.collect {
            when (it) {
                is GameHomeEvent.Back -> onBackPressed()
            }
        }
    }

    GameHomeScreen(viewModel::handleIntent)
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun GameHomeScreen(
    postIntent: (GameHomeIntent) -> Unit
) {
    BottomSheetScaffold(
        topBar = {
            GameHomeTopBar(
                title = stringResource(R.string.game_home_label),
                icon = AppIcons.Close,
                onBackPressed = { postIntent(GameHomeIntent.Back) }
            )
        },
        modifier = Modifier.fillMaxSize(),
        sheetContent = {}
    ) {
        Box(modifier = Modifier.padding(it), propagateMinConstraints = true) {
        }
    }
}

@Preview
@Composable
internal fun GameHomePreview() {
    AppTheme { GameHomeScreen {} }
}
