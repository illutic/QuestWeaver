package g.sig.questweaver.game.home.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.core.view.HapticFeedbackConstantsCompat
import androidx.hilt.navigation.compose.hiltViewModel
import g.sig.questweaver.common.ui.components.CenteredProgressBar
import g.sig.questweaver.common.ui.mappers.getStrokeWidth
import g.sig.questweaver.common.ui.mappers.toPath
import g.sig.questweaver.common.ui.mappers.toPoint
import g.sig.questweaver.domain.entities.blocks.Point
import g.sig.questweaver.domain.entities.common.TransformationData
import g.sig.questweaver.domain.entities.common.User
import g.sig.questweaver.game.home.R
import g.sig.questweaver.game.home.data.GameHomeViewModel
import g.sig.questweaver.game.home.screens.components.Annotation
import g.sig.questweaver.game.home.screens.components.AnnotationTools
import g.sig.questweaver.game.home.screens.components.ColorPicker
import g.sig.questweaver.game.home.screens.components.GameHomeTopBar
import g.sig.questweaver.game.home.screens.components.HomeEditControls
import g.sig.questweaver.game.home.state.GameHomeEvent
import g.sig.questweaver.game.home.state.GameHomeIntent
import g.sig.questweaver.game.home.state.GameHomeState
import g.sig.questweaver.ui.AppIcons
import g.sig.questweaver.ui.AppTheme
import g.sig.questweaver.ui.largeSize

@Composable
internal fun GameHomeRoute(onBackPress: () -> Unit) {
    val viewModel = hiltViewModel<GameHomeViewModel>()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(onBackPress) {
        viewModel.handleIntent(GameHomeIntent.Load)
        viewModel.events.collect {
            when (it) {
                is GameHomeEvent.Back -> onBackPress()
            }
        }
    }

    GameHomeScreen(state, viewModel::handleIntent)
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun GameHomeScreen(
    state: GameHomeState,
    postIntent: (GameHomeIntent) -> Unit,
) {
    val scaffoldState = rememberBottomSheetScaffoldState()

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        topBar = {
            GameHomeTopBar(
                title = stringResource(R.string.game_home_label),
                icon = AppIcons.Close,
                onBackPress = { postIntent(GameHomeIntent.Back) },
            )
        },
        sheetContent = {
            when (state) {
                is GameHomeState.Loaded -> GameHomeScreenSheetContent(state, postIntent)
                is GameHomeState.Loading -> Unit
            }
        },
    ) { padding ->
        when (state) {
            is GameHomeState.Loaded -> {
                if (state.showColorPicker) {
                    ColorPicker(
                        initialColor = state.selectedColor,
                        onDismiss = { postIntent(GameHomeIntent.HideColorPicker) },
                        onColorSelect = { postIntent(GameHomeIntent.SelectColor(it)) },
                    )
                }

                GameHomeBottomSheetControls(bottomSheetScaffoldState = scaffoldState, state = state)

                GameHomeScreenContent(
                    state = state,
                    postIntent = postIntent,
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(padding),
                )
            }

            is GameHomeState.Loading -> CenteredProgressBar()
        }
    }
}

@Composable
private fun GameHomeScreenContent(
    state: GameHomeState.Loaded,
    postIntent: (GameHomeIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current
    var canvasSize by remember { mutableStateOf(Size.Zero) }
    val currentlyDrawnPoints = remember { mutableStateListOf<Point>() }

    BoxWithConstraints(
        modifier =
            modifier
                .annotateDrawing(
                    state,
                    canvasSize,
                    currentlyDrawnPoints,
                    postIntent,
                ).pointerInput(state) {
                    detectTapGestures {
                        if (state.annotationMode != GameHomeState.AnnotationMode.TextMode) return@detectTapGestures
                        postIntent(
                            GameHomeIntent.AddText(
                                TransformationData(anchor = it.toPoint(canvasSize)),
                            ),
                        )
                    }
                },
    ) {
        val size = remember(maxWidth, maxHeight) { DpSize(maxWidth, maxHeight) }
        canvasSize = remember(density, size) { with(density) { size.toSize() } }

        state.annotations.values.forEach { annotation ->
            Annotation(
                annotation,
                canvasSize = canvasSize,
                mode = state.annotationMode,
                userId = state.currentUser.id,
                isDm = state.isDM,
                postIntent = postIntent,
            )
        }

        Canvas(
            modifier =
                Modifier
                    .matchParentSize()
                    .annotateDrawing(state, canvasSize, currentlyDrawnPoints, postIntent),
        ) {
            if (state.annotationMode == GameHomeState.AnnotationMode.DrawingMode) {
                drawPath(
                    path = currentlyDrawnPoints.toPath(canvasSize),
                    color = state.selectedColor,
                    alpha = state.selectedColor.alpha,
                    style = state.selectedSize.getStrokeWidth(canvasSize),
                )
            }
        }
    }
}

@Composable
private fun GameHomeScreenSheetContent(
    state: GameHomeState.Loaded,
    postIntent: (GameHomeIntent) -> Unit,
) {
    Column(
        modifier =
        Modifier
            .fillMaxWidth()
            .padding(largeSize),
    ) {
        AnnotationTools(
            modifier = Modifier.fillMaxWidth(),
            annotationMode = state.annotationMode,
            isDM = state.isDM,
            allowEditing = state.allowAnnotations,
            onAnnotationModeChange = { postIntent(GameHomeIntent.ChangeMode(it)) },
        )

        when (state.annotationMode) {
            GameHomeState.AnnotationMode.TextMode,
            GameHomeState.AnnotationMode.DrawingMode -> {
                HomeEditControls(
                    state = state,
                    postIntent = postIntent,
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            GameHomeState.AnnotationMode.Idle,
            GameHomeState.AnnotationMode.RemoveMode -> Unit

            GameHomeState.AnnotationMode.DMMode -> TODO()
        }
    }
}

private fun Modifier.annotateDrawing(
    state: GameHomeState.Loaded,
    canvasSize: Size,
    drawnPoints: MutableList<Point>,
    postIntent: (GameHomeIntent) -> Unit,
) = composed {
    val view = LocalView.current
    if (state.annotationMode != GameHomeState.AnnotationMode.DrawingMode) return@composed this

    pointerInput(state.annotationMode) {
        detectDragGestures(
            onDragEnd = {
                postIntent(GameHomeIntent.AddDrawing(drawnPoints, state.selectedSize))
                drawnPoints.clear()
            },
            onDragStart = { drawnPoints.clear() },
            onDragCancel = { drawnPoints.clear() },
        ) { change, _ ->
            view.performHapticFeedback(HapticFeedbackConstantsCompat.SEGMENT_FREQUENT_TICK)
            val point = change.position.toPoint(canvasSize)
            drawnPoints.add(point)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GameHomeBottomSheetControls(
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    state: GameHomeState.Loaded,
) {
    val bottomSheetState = bottomSheetScaffoldState.bottomSheetState
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(state.annotationMode) {
        when (state.annotationMode) {
            GameHomeState.AnnotationMode.TextMode,
            GameHomeState.AnnotationMode.DrawingMode,
            -> bottomSheetState.expand()

            else -> bottomSheetState.expand()
        }
    }

    LaunchedEffect(bottomSheetState.targetValue) {
        keyboardController?.hide()
    }
}

@Preview
@Composable
internal fun GameHomePreview() {
    AppTheme { GameHomeScreen(GameHomeState.Loaded(User("", ""))) {} }
}
