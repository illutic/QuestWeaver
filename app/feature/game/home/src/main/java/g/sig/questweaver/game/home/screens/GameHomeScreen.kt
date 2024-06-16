package g.sig.questweaver.game.home.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import g.sig.questweaver.game.home.R
import g.sig.questweaver.game.home.data.GameHomeViewModel
import g.sig.questweaver.game.home.screens.components.AnnotationTools
import g.sig.questweaver.game.home.screens.components.GameHomeTopBar
import g.sig.questweaver.game.home.state.GameHomeEvent
import g.sig.questweaver.game.home.state.GameHomeIntent
import g.sig.questweaver.game.home.state.GameHomeState
import g.sig.questweaver.ui.AppIcons
import g.sig.questweaver.ui.AppTheme
import g.sig.questweaver.ui.mediumSize

@Composable
internal fun GameHomeRoute(onBackPressed: () -> Unit) {
    val viewModel = hiltViewModel<GameHomeViewModel>()

    LaunchedEffect(Unit) {
        viewModel.handleIntent(GameHomeIntent.Load)
        viewModel.events.collect {
            when (it) {
                is GameHomeEvent.Back -> onBackPressed()
            }
        }
    }

    GameHomeScreen(viewModel.state, viewModel::handleIntent)
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun GameHomeScreen(
    state: GameHomeState,
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
        sheetPeekHeight = BottomSheetDefaults.SheetPeekHeight + 56.dp,
        sheetContent = {
            Column(modifier = Modifier.padding(horizontal = mediumSize)) {
                AnnotationTools(
                    modifier = Modifier.fillMaxWidth(),
                    annotationMode = state.annotationMode,
                    isDM = state.isDM,
                    allowEditing = state.allowAnnotations,
                    onAnnotationModeChanged = { postIntent(GameHomeIntent.ChangeMode(it)) }
                )
            }
        }
    ) {
        Box(modifier = Modifier.padding(it), propagateMinConstraints = true) {

        }
    }
}

@Preview
@Composable
internal fun GameHomePreview() {
    AppTheme { GameHomeScreen(GameHomeState()) {} }
}
