package g.sig.questweaver.game.home.screens.components

import android.content.pm.ApplicationInfo
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import g.sig.questweaver.common.ui.components.ImageWithPlaceholder
import g.sig.questweaver.common.ui.mappers.toPath
import g.sig.questweaver.common.ui.mappers.toPoint
import g.sig.questweaver.domain.entities.common.Annotation
import g.sig.questweaver.game.home.data.Drawing
import g.sig.questweaver.game.home.data.Image
import g.sig.questweaver.game.home.data.Text
import g.sig.questweaver.game.home.data.toUiItem
import g.sig.questweaver.game.home.state.GameHomeIntent
import g.sig.questweaver.game.home.state.GameHomeState
import g.sig.questweaver.ui.MediumRoundedShape
import g.sig.questweaver.ui.defaultAnimationSpec
import kotlinx.coroutines.delay

private const val COMMIT_DELAY = 500L

@Composable
fun Annotation(
    annotation: Annotation,
    mode: GameHomeState.AnnotationMode,
    isDm: Boolean,
    userId: String,
    modifier: Modifier = Modifier,
    canvasSize: Size = Size.Unspecified,
    postIntent: (GameHomeIntent) -> Unit,
) {
    val applicationInfo = LocalContext.current.applicationInfo
    val isDebuggable = 0 != applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE
    val allowTransformations = userId == annotation.createdBy || isDm
    var scale by remember { mutableFloatStateOf(annotation.transformationData.scale) }
    var rotation by remember { mutableFloatStateOf(annotation.transformationData.rotation) }
    var anchor by remember { mutableStateOf(annotation.transformationData.anchor) }
    var changesInProgress by remember { mutableStateOf(false) }
    val border = if (isDebuggable) BorderStroke(1.dp, Color.Red) else null
    val onPositionChange by rememberUpdatedState { offset: Offset ->
        changesInProgress = true
        anchor = offset.toPoint(canvasSize)
    }
    val onScaleChange by rememberUpdatedState { newScale: Float ->
        changesInProgress = true
        scale = newScale
    }
    val onRotationChange by rememberUpdatedState { newRotation: Float ->
        changesInProgress = true
        rotation = newRotation
    }
    val onClick by rememberUpdatedState {
        postIntent(GameHomeIntent.SelectAnnotation(annotation))
    }

    LaunchedEffect(scale, rotation, anchor, changesInProgress, postIntent) {
        if (changesInProgress) {
            delay(COMMIT_DELAY)
            changesInProgress = false
            postIntent(
                GameHomeIntent.CommitTransformation(
                    annotation.id,
                    scale,
                    rotation,
                    anchor,
                ),
            )
        }
    }

    when (val annotationUiItem = annotation.toUiItem(canvasSize)) {
        is Drawing ->
            DrawingAnnotation(
                modifier = modifier,
                annotation = annotationUiItem,
                allowTransformations = allowTransformations,
                onPositionChange = onPositionChange,
                onScaleChange = onScaleChange,
                onRotationChange = onRotationChange,
                border = border,
                onClick = onClick,
            )

        is Image ->
            ImageAnnotation(
                modifier = modifier,
                image = annotationUiItem,
                allowTransformations = allowTransformations,
                onPositionChange = onPositionChange,
                onScaleChange = onScaleChange,
                onRotationChange = onRotationChange,
                border = border,
                onClick = onClick,
            )

        is Text ->
            TextAnnotation(
                modifier = modifier,
                allowEditing = allowTransformations && mode == GameHomeState.AnnotationMode.TextMode,
                allowTransformations = allowTransformations,
                annotationUiItem = annotationUiItem,
                onPositionChange = onPositionChange,
                onScaleChange = onScaleChange,
                onRotationChange = onRotationChange,
                border = border,
                onClick = onClick,
                onTextChange = {
                    postIntent(GameHomeIntent.ChangeText(annotation.id, it))
                },
            )
    }
}

@Composable
private fun DrawingAnnotation(
    annotation: Drawing,
    modifier: Modifier = Modifier,
    allowTransformations: Boolean = true,
    onPositionChange: (Offset) -> Unit = {},
    onScaleChange: (Float) -> Unit = {},
    onRotationChange: (Float) -> Unit = {},
    onClick: () -> Unit = {},
    border: BorderStroke? = null,
) {
    val topLeft = Offset(annotation.path.minOf { it.x }, annotation.path.minOf { it.y })
    val bottomRight = Offset(annotation.path.maxOf { it.x }, annotation.path.maxOf { it.y })
    val shapeSize =
        Size(
            width = bottomRight.x - topLeft.x,
            height = bottomRight.y - topLeft.y,
        )
    val shapeSizeDp = with(LocalDensity.current) { shapeSize.toDpSize() }

    Transformable(
        modifier = modifier,
        initialAngle = annotation.transformationData.rotation,
        initialZoom = annotation.transformationData.scale,
        initialOffset = annotation.transformationData.anchor,
        allowTransformations = allowTransformations,
        onPositionChange = onPositionChange,
        onScaleChange = onScaleChange,
        onRotationChange = onRotationChange,
        onClick = onClick,
        border = border,
    ) {
        Canvas(Modifier.size(shapeSizeDp)) {
            drawPath(
                path = annotation.path.toPath(),
                color = annotation.color,
                alpha = annotation.color.alpha,
                style = annotation.stroke,
            )
        }
    }
}

@Composable
private fun TextAnnotation(
    allowEditing: Boolean,
    annotationUiItem: Text,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    allowTransformations: Boolean = true,
    onPositionChange: (Offset) -> Unit = {},
    onScaleChange: (Float) -> Unit = {},
    onRotationChange: (Float) -> Unit = {},
    onClick: () -> Unit = {},
    border: BorderStroke? = null,
) {
    var internalText by remember(annotationUiItem.text) { mutableStateOf(annotationUiItem.text) }

    LaunchedEffect(internalText, onTextChange) {
        if (internalText != annotationUiItem.text) {
            delay(COMMIT_DELAY)
            onTextChange(internalText)
        }
    }

    Transformable(
        modifier = modifier,
        initialAngle = annotationUiItem.transformationData.rotation,
        initialZoom = annotationUiItem.transformationData.scale,
        initialOffset = annotationUiItem.transformationData.anchor,
        allowTransformations = allowTransformations,
        onPositionChange = onPositionChange,
        onScaleChange = onScaleChange,
        onRotationChange = onRotationChange,
        onClick = onClick,
        border = border,
    ) {
        AnimatedContent(
            targetState = allowEditing,
            transitionSpec = {
                fadeIn(defaultAnimationSpec()) togetherWith fadeOut(defaultAnimationSpec())
            },
            label = "textAnnotationAnimation",
        ) { allowEdit ->
            if (allowEdit) {
                TextField(
                    value = internalText,
                    shape = MediumRoundedShape,
                    onValueChange = { internalText = it },
                )
            } else {
                Text(
                    modifier =
                        Modifier
                            .defaultMinSize(
                                minWidth = TextFieldDefaults.MinWidth,
                                minHeight = TextFieldDefaults.MinHeight,
                            ).padding(TextFieldDefaults.contentPaddingWithoutLabel()),
                    text = internalText,
                )
            }
        }
    }
}

@Composable
private fun ImageAnnotation(
    image: Image,
    modifier: Modifier = Modifier,
    allowTransformations: Boolean = true,
    onPositionChange: (Offset) -> Unit = {},
    onScaleChange: (Float) -> Unit = {},
    onRotationChange: (Float) -> Unit = {},
    onClick: () -> Unit = {},
    border: BorderStroke? = null,
) {
    Transformable(
        modifier = modifier,
        initialAngle = image.transformationData.rotation,
        initialZoom = image.transformationData.scale,
        initialOffset = image.transformationData.anchor,
        allowTransformations = allowTransformations,
        onPositionChange = onPositionChange,
        onScaleChange = onScaleChange,
        onRotationChange = onRotationChange,
        onClick = onClick,
        border = border,
    ) {
        ImageWithPlaceholder(model = image.uri)
    }
}
