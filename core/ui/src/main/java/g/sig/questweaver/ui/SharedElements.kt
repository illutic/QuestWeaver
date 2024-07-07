package g.sig.questweaver.ui

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed

@OptIn(ExperimentalSharedTransitionApi::class)
val LocalSharedTransitionScope =
    staticCompositionLocalOf<SharedTransitionScope> {
        throw IllegalStateException("No SharedTransitionScope has been provided.")
    }

@OptIn(ExperimentalSharedTransitionApi::class)
fun Modifier.sharedElement(
    key: String,
    animationScope: AnimatedContentScope,
) = composed {
    with(LocalSharedTransitionScope.current) {
        sharedElement(
            rememberSharedContentState(key),
            animationScope,
        )
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
fun Modifier.sharedBounds(
    key: String,
    animationScope: AnimatedContentScope,
) = composed {
    with(LocalSharedTransitionScope.current) {
        sharedBounds(
            rememberSharedContentState(key),
            animationScope,
            boundsTransform = { _, _ -> defaultAnimationSpec() },
            enter = defaultNavigationEnterTransition,
            exit = defaultNavigationExitTransition,
        )
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
inline fun SharedTransitionsLayout(crossinline content: @Composable () -> Unit) {
    SharedTransitionLayout {
        CompositionLocalProvider(LocalSharedTransitionScope provides this) {
            content()
        }
    }
}
