package g.sig.questweaver.onboarding.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import g.sig.questweaver.common.ui.components.ImageWithPlaceholder
import g.sig.questweaver.common.ui.layouts.ScreenScaffold
import g.sig.questweaver.navigation.SharedElementKeys
import g.sig.questweaver.onboarding.R
import g.sig.questweaver.ui.largeSize
import g.sig.questweaver.ui.sharedElement

@Composable
internal fun ExplanationScreen(
    animationScope: AnimatedContentScope,
    onNavigateToUserCreation: () -> Unit,
) {
    ScreenScaffold(
        navigation = {
            ExplanationButton(
                modifier =
                    Modifier.sharedElement(
                        SharedElementKeys.WELCOME_CTA_KEY,
                        animationScope,
                    ),
                onNavigateToUserCreation = onNavigateToUserCreation,
            )
        },
    ) {
        val scrollState = rememberScrollState()

        ImageWithPlaceholder(
            modifier =
                Modifier
                    .sharedElement(SharedElementKeys.WELCOME_LOGO_KEY, animationScope)
                    .verticalScroll(scrollState),
            size = ExplanationSize.imageSize,
            model = R.drawable.graphic_1,
            contentDescription = null,
        )

        Column(
            modifier =
                Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(it)
                    .padding(largeSize),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(largeSize),
        ) {
            Text(
                modifier =
                    Modifier.sharedElement(
                        SharedElementKeys.WELCOME_APP_NAME_KEY,
                        animationScope,
                    ),
                text = stringResource(id = R.string.explanation_title),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
            )

            Text(
                text = stringResource(id = R.string.explanation_header_1),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
            )

            Text(
                text = stringResource(id = R.string.explanation_body_1),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
            )

            HorizontalDivider()

            Text(
                text = stringResource(id = R.string.explanation_header_2),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
            )

            Text(
                text = stringResource(id = R.string.explanation_body_2),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
            )

            HorizontalDivider()
        }
    }
}

@Composable
private fun ExplanationButton(
    onNavigateToUserCreation: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = largeSize),
        onClick = { onNavigateToUserCreation() },
    ) {
        Text(text = stringResource(id = R.string.explanation_cta))
    }
}

@Composable
@Preview
private fun ExplanationScreenPreview() {
    AnimatedContent(targetState = true, label = "preview") {
        if (it) {
            ExplanationScreen(this, onNavigateToUserCreation = {})
        }
    }
}
