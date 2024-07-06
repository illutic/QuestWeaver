package g.sig.questweaver.ui

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith

fun <T> defaultAnimationSpec() = spring<T>(
    dampingRatio = Spring.DampingRatioLowBouncy,
    stiffness = Spring.StiffnessMediumLow
)

fun <T> defaultQuickAnimationSpec() = spring<T>(
    dampingRatio = Spring.DampingRatioNoBouncy,
    stiffness = Spring.StiffnessMedium
)

val defaultEnterTransition = fadeIn(defaultAnimationSpec()) + scaleIn(defaultAnimationSpec())

val defaultExitTransition = fadeOut(defaultAnimationSpec()) + scaleOut(defaultAnimationSpec())

val defaultContentTransform = defaultEnterTransition togetherWith defaultExitTransition

val defaultNavigationEnterTransition = fadeIn(defaultQuickAnimationSpec())

val defaultNavigationExitTransition = fadeOut(defaultQuickAnimationSpec())
