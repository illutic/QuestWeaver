package g.sig.questweaver.common.ui.components

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import g.sig.questweaver.ui.LargeRoundedShape

@Composable
fun ImageWithPlaceholder(
    model: Any?,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    contentScale: ContentScale = ContentScale.Fit
) {
    var showShimmer by remember { mutableStateOf(true) }
    AsyncImage(
        model = model,
        contentDescription = contentDescription,
        contentScale = contentScale,
        modifier = modifier
            .background(shimmerBrush(showShimmer), shape = LargeRoundedShape),
        onSuccess = { showShimmer = false },
        onLoading = { showShimmer = true },
        onError = { showShimmer = false }
    )
}
