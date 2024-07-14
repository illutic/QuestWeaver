package g.sig.questweaver.common.ui.components

import android.content.Context
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import g.sig.questweaver.common.ui.mappers.getStrokeWidth
import g.sig.questweaver.common.ui.mappers.load
import g.sig.questweaver.common.ui.mappers.toComposeColor
import g.sig.questweaver.common.ui.mappers.toOffset
import g.sig.questweaver.common.ui.mappers.toPath
import g.sig.questweaver.common.ui.mappers.toSp
import g.sig.questweaver.domain.entities.common.Annotation

fun DrawScope.drawAnnotations(
    annotations: Iterable<Annotation>,
    textMeasurer: TextMeasurer,
    textStyle: TextStyle,
    context: Context,
) {
    annotations.forEach { annotation ->
        when (annotation) {
            is Annotation.Drawing ->
                drawPath(
                    path = annotation.path.toPath(size),
                    color = annotation.color.toComposeColor(),
                    alpha = annotation.color.toComposeColor().alpha,
                    style = annotation.strokeSize.getStrokeWidth(size),
                )

            is Annotation.Image ->
                drawImage(
                    image = annotation.load(context),
                    topLeft = annotation.anchor.toOffset(size),
                )

            is Annotation.Text -> {
                drawText(
                    textMeasurer = textMeasurer,
                    text = annotation.text,
                    style =
                        textStyle.merge(
                            color = annotation.color.toComposeColor(),
                            fontSize = annotation.size.toSp(size).toSp(),
                        ),
                    topLeft = annotation.anchor.toOffset(size),
                )
            }
        }
    }
}
