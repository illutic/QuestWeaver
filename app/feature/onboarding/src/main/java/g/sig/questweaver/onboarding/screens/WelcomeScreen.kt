package g.sig.questweaver.onboarding.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import g.sig.questweaver.common.ui.layouts.ScreenScaffold
import g.sig.questweaver.navigation.SharedElementKeys
import g.sig.questweaver.onboarding.R
import g.sig.questweaver.ui.AppIcons
import g.sig.questweaver.ui.largeSize
import g.sig.questweaver.ui.sharedElement
import g.sig.questweaver.ui.xLargeSize
import kotlinx.coroutines.delay

@Composable
fun WelcomeScreen(
    animationScope: AnimatedContentScope,
    onNavigateNext: () -> Unit,
) {
    var showTitle by rememberSaveable { mutableStateOf(false) }
    var showContent by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(500)
        showTitle = true
    }

    LaunchedEffect(Unit) {
        delay(1000)
        showContent = true
    }

    ScreenScaffold {
        Column(
            modifier =
                Modifier
                    .padding(it)
                    .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                modifier =
                    Modifier
                        .sharedElement(
                            SharedElementKeys.WELCOME_LOGO_KEY,
                            animationScope,
                        ).size(200.dp),
                painter = AppIcons.Logo,
                contentDescription = "QuestWeaver",
            )

            AnimatedVisibility(
                visible = showTitle,
            ) {
                Text(
                    modifier =
                        Modifier.sharedElement(
                            SharedElementKeys.WELCOME_APP_NAME_KEY,
                            animationScope,
                        ),
                    text = stringResource(id = R.string.app_name),
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
            }

            AnimatedVisibility(
                modifier = Modifier.padding(vertical = xLargeSize),
                visible = showContent,
            ) {
                Button(
                    modifier =
                        Modifier
                            .padding(horizontal = largeSize)
                            .sharedElement(SharedElementKeys.WELCOME_CTA_KEY, animationScope),
                    onClick = { onNavigateNext() },
                ) {
                    Text(text = stringResource(id = R.string.explanation_cta))
                }
            }
        }
    }
}

@Composable
@Preview
private fun WelcomeScreenPreview() {
    AnimatedContent(targetState = true, label = "preview") {
        if (it) {
            WelcomeScreen(this, onNavigateNext = {})
        }
    }
}
