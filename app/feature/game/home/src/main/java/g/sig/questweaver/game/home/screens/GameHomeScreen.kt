package g.sig.questweaver.game.home.screens

import android.content.pm.ApplicationInfo
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.HapticFeedbackConstantsCompat
import androidx.hilt.navigation.compose.hiltViewModel
import g.sig.questweaver.common.ui.components.AppOutlinedTextField
import g.sig.questweaver.common.ui.components.drawAnnotations
import g.sig.questweaver.common.ui.mappers.getBounds
import g.sig.questweaver.common.ui.mappers.getClickedAnnotation
import g.sig.questweaver.common.ui.mappers.getStrokeWidth
import g.sig.questweaver.common.ui.mappers.toPath
import g.sig.questweaver.common.ui.mappers.toPoint
import g.sig.questweaver.common.ui.mappers.toSize
import g.sig.questweaver.common.ui.mappers.toSp
import g.sig.questweaver.domain.entities.blocks.Point
import g.sig.questweaver.domain.entities.common.Annotation
import g.sig.questweaver.game.home.R
import g.sig.questweaver.game.home.data.GameHomeViewModel
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
    postIntent: (GameHomeIntent) -> Unit,
) {
    val scaffoldState = rememberBottomSheetScaffoldState()
    val textMeasurer = rememberTextMeasurer()
    val textStyle = LocalTextStyle.current
    val context = LocalContext.current

    if (state.showColorPicker) {
        ColorPicker(
            initialColor = state.selectedColor,
            onDismiss = { state.showColorPicker = false },
            onColorSelected = { postIntent(GameHomeIntent.SelectColor(it)) },
        )
    }

    GameHomeBottomSheetControls(bottomSheetScaffoldState = scaffoldState, state = state)

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        topBar = {
            GameHomeTopBar(
                title = stringResource(R.string.game_home_label),
                icon = AppIcons.Close,
                onBackPressed = { postIntent(GameHomeIntent.Back) },
            )
        },
        sheetContent = { GameHomeScreenSheetContent(state, postIntent) },
    ) {
        val applicationInfo = context.applicationInfo
        val isDebuggable = 0 != applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE
        var canvasSize by remember { mutableStateOf(Size.Zero) }
        val currentlyDrawnPoints = remember { mutableStateListOf<Point>() }
        val isDrawingMode by remember(state.annotationMode) {
            derivedStateOf { state.annotationMode == GameHomeState.AnnotationMode.DrawingMode }
        }

        Canvas(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(it)
                    .selectAnnotations(state, canvasSize, postIntent)
                    .annotateDrawing(state, canvasSize, currentlyDrawnPoints, postIntent)
                    .annotateText(state, textMeasurer, textStyle, canvasSize, postIntent),
        ) {
            canvasSize = size
            drawAnnotations(state.annotations.values, textMeasurer, textStyle, context)
            if (isDebuggable) {
                drawAnnotationBounds(state.annotations.values.toList())
            }

            if (isDrawingMode) {
                drawPath(
                    path = currentlyDrawnPoints.toPath(size),
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
    state: GameHomeState,
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
            onAnnotationModeChanged = { postIntent(GameHomeIntent.ChangeMode(it)) },
        )

        when (state.annotationMode) {
            GameHomeState.AnnotationMode.DrawingMode -> {
                HomeEditControls(
                    state = state,
                    postIntent = postIntent,
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            GameHomeState.AnnotationMode.TextMode -> {
                AppOutlinedTextField(
                    value = state.selectedText,
                    onValueChanged = { state.selectedText = it },
                    modifier =
                        Modifier
                            .padding(top = largeSize)
                            .fillMaxWidth(),
                )

                HomeEditControls(
                    state = state,
                    postIntent = postIntent,
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            GameHomeState.AnnotationMode.Idle,
            GameHomeState.AnnotationMode.RemoveMode,
            -> Unit

            GameHomeState.AnnotationMode.DMMode -> TODO()
        }
    }
}

private fun Modifier.annotateText(
    state: GameHomeState,
    textMeasurer: TextMeasurer,
    localTextStyle: TextStyle,
    canvasSize: Size,
    postIntent: (GameHomeIntent) -> Unit,
) = composed {
    val view = LocalView.current
    pointerInput(state.annotationMode) {
        if (state.annotationMode != GameHomeState.AnnotationMode.TextMode) return@pointerInput
        detectTapGestures { offset ->
            val result =
                textMeasurer.measure(
                    text = state.selectedText,
                    style =
                        localTextStyle.merge(
                            color = state.selectedColor,
                            fontSize = state.selectedSize.toSp(canvasSize).toSp(),
                    ),
                )

            view.performHapticFeedback(HapticFeedbackConstantsCompat.SEGMENT_FREQUENT_TICK)
            postIntent(
                GameHomeIntent.AddText(
                    state.selectedText,
                    result.size.toSize(size),
                    offset.toPoint(canvasSize),
                ),
            )
        }
    }
}

private fun Modifier.annotateDrawing(
    state: GameHomeState,
    canvasSize: Size,
    drawnPoints: MutableList<Point>,
    postIntent: (GameHomeIntent) -> Unit,
) = composed {
    val view = LocalView.current

    pointerInput(state.annotationMode) {
        if (state.annotationMode != GameHomeState.AnnotationMode.DrawingMode) return@pointerInput
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

private fun Modifier.selectAnnotations(
    state: GameHomeState,
    canvasSize: Size,
    postIntent: (GameHomeIntent) -> Unit,
) = composed {
    val view = LocalView.current

    pointerInput(state.annotationMode) {
        if (state.annotationMode != GameHomeState.AnnotationMode.RemoveMode) return@pointerInput
        detectTapGestures { offset ->
            state.annotations.values.getClickedAnnotation(offset, canvasSize)?.let {
                view.performHapticFeedback(HapticFeedbackConstantsCompat.SEGMENT_FREQUENT_TICK)
                postIntent(GameHomeIntent.SelectAnnotation(it))
            }
        }
    }
}

private fun DrawScope.drawAnnotationBounds(annotations: List<Annotation>) {
    annotations.forEach { annotation ->
        val bounds = annotation.getBounds(size)
        drawRect(
            color = Color.Red,
            topLeft = bounds.topLeft,
            size = bounds.size,
            style = Stroke(2f),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GameHomeBottomSheetControls(
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    state: GameHomeState,
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
    AppTheme { GameHomeScreen(GameHomeState()) {} }
}
