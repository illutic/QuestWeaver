package g.sig.questweaver.common.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import coil.compose.AsyncImage
import g.sig.questweaver.ui.LargeRoundedShape

@Composable
fun ImageWithPlaceholder(
    model: Any?,
    modifier: Modifier = Modifier,
    size: Dp = Dp.Unspecified,
    contentDescription: String? = null,
    contentScale: ContentScale = ContentScale.Fit,
) {
    var showShimmer by remember { mutableStateOf(true) }
    Box(
        modifier = modifier.background(shimmerBrush(showShimmer), shape = LargeRoundedShape),
        propagateMinConstraints = true,
    ) {
        AsyncImage(
            model = model,
            contentDescription = contentDescription,
            contentScale = contentScale,
            modifier = Modifier.size(size),
            onSuccess = { showShimmer = false },
            onLoading = { showShimmer = true },
            onError = { showShimmer = false },
        )
    }
}
