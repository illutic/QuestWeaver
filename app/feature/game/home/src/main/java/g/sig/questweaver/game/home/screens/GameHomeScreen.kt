package g.sig.questweaver.game.home.screens

import android.content.Context
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import g.sig.questweaver.common.ui.mappers.getClickedAnnotation
import g.sig.questweaver.common.ui.mappers.getStrokeWidth
import g.sig.questweaver.common.ui.mappers.load
import g.sig.questweaver.common.ui.mappers.toComposeColor
import g.sig.questweaver.common.ui.mappers.toComposeSize
import g.sig.questweaver.common.ui.mappers.toOffset
import g.sig.questweaver.common.ui.mappers.toPath
import g.sig.questweaver.common.ui.mappers.toPoint
import g.sig.questweaver.domain.entities.blocks.Point
import g.sig.questweaver.domain.entities.common.Annotation
import g.sig.questweaver.game.home.R
import g.sig.questweaver.game.home.data.GameHomeViewModel
import g.sig.questweaver.game.home.screens.components.AnnotationTools
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
    postIntent: (GameHomeIntent) -> Unit
) {
    val scaffoldState = rememberBottomSheetScaffoldState()
    val textMeasurer = rememberTextMeasurer()
    val textStyle = LocalTextStyle.current
    val context = LocalContext.current

    LaunchedEffect(state.annotationMode) {
        when (state.annotationMode) {
            GameHomeState.AnnotationMode.TextMode,
            GameHomeState.AnnotationMode.DrawingMode -> scaffoldState.bottomSheetState.expand()

            else -> scaffoldState.bottomSheetState.expand()
        }
    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        topBar = {
            GameHomeTopBar(
                title = stringResource(R.string.game_home_label),
                icon = AppIcons.Close,
                onBackPressed = { postIntent(GameHomeIntent.Back) }
            )
        },
        sheetContent = { GameHomeScreenSheetContent(state, postIntent) }
    ) {
        var canvasSize by remember { mutableStateOf(Size.Zero) }
        val currentlyDrawnPoints = remember { mutableStateListOf<Point>() }

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .clickAnnotation(annotations = state.annotations, canvasSize = canvasSize) {
                    postIntent(GameHomeIntent.SelectAnnotation(it))
                }
                .addAnnotations(
                    shouldDraw = state.annotationMode == GameHomeState.AnnotationMode.DrawingMode,
                    canvasSize = canvasSize,
                    drawnPoints = currentlyDrawnPoints,
                ) { points ->
                    postIntent(
                        GameHomeIntent.AddDrawing(path = points, strokeSize = state.selectedSize)
                    )
                }
                .addText()
        ) {
            canvasSize = size
            drawAnnotations(state, textMeasurer, textStyle, context)

            if (state.annotationMode == GameHomeState.AnnotationMode.DrawingMode) {
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
    postIntent: (GameHomeIntent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(largeSize)
    ) {
        AnnotationTools(
            modifier = Modifier.fillMaxWidth(),
            annotationMode = state.annotationMode,
            isDM = state.isDM,
            allowEditing = state.allowAnnotations,
            onAnnotationModeChanged = { postIntent(GameHomeIntent.ChangeMode(it)) }
        )

        when (state.annotationMode) {
            GameHomeState.AnnotationMode.TextMode,
            GameHomeState.AnnotationMode.DrawingMode -> {
                HomeEditControls(
                    state = state,
                    postIntent = postIntent,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            GameHomeState.AnnotationMode.Idle,
            GameHomeState.AnnotationMode.RemoveMode -> Unit

            GameHomeState.AnnotationMode.DMMode -> TODO()
        }
    }
}

private fun DrawScope.drawAnnotations(
    state: GameHomeState,
    textMeasurer: TextMeasurer,
    textStyle: TextStyle,
    context: Context
) {
    state.annotations.forEach { annotation ->
        when (annotation) {
            is Annotation.Drawing -> drawPath(
                path = annotation.path.toPath(size),
                color = annotation.color.toComposeColor(),
                alpha = annotation.color.toComposeColor().alpha,
                style = annotation.strokeSize.getStrokeWidth(size),
            )

            is Annotation.Image -> drawImage(
                image = annotation.load(context),
                topLeft = annotation.anchor.toOffset(size),
            )

            is Annotation.Text -> drawText(
                textMeasurer = textMeasurer,
                text = annotation.text,
                topLeft = annotation.anchor.toOffset(size),
                size = annotation.size.toComposeSize(size),
                style = textStyle.copy(color = annotation.color.toComposeColor()),
            )
        }
    }
}

private fun Modifier.clickAnnotation(
    annotations: List<Annotation>,
    canvasSize: Size,
    onAnnotationClicked: (Annotation) -> Unit
) = pointerInput(Unit) {
    detectTapGestures { offset ->
        annotations.getClickedAnnotation(offset, canvasSize)?.let(onAnnotationClicked)
    }
}

private fun Modifier.addAnnotations(
    shouldDraw: Boolean,
    canvasSize: Size,
    drawnPoints: MutableList<Point>,
    onDrawingComplete: (List<Point>) -> Unit
) = pointerInput(Unit) {
    detectDragGestures(
        onDragEnd = {
            onDrawingComplete(drawnPoints)
            drawnPoints.clear()
        },
        onDragStart = { drawnPoints.clear() },
        onDragCancel = { drawnPoints.clear() }
    ) { change, _ ->
        if (shouldDraw) {
            val point = change.position.toPoint(canvasSize)
            drawnPoints.add(point)
        }
    }
}

private fun Modifier.addText() = pointerInput(Unit) {
    detectTapGestures { offset ->

    }
}

@Preview
@Composable
internal fun GameHomePreview() {
    AppTheme { GameHomeScreen(GameHomeState()) {} }
}
