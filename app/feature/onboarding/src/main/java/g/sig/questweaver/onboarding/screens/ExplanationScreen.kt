package g.sig.questweaver.onboarding.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import g.sig.questweaver.onboarding.R
import g.sig.questweaver.ui.largeSize

@Composable
internal fun ExplanationScreen(onNavigateToUserCreation: () -> Unit) {
    ScreenScaffold(
        navigation = {
            ExplanationButton(onNavigateToUserCreation)
        },
    ) {
        val scrollState = rememberScrollState()

        ImageWithPlaceholder(
            modifier =
                Modifier
                    .verticalScroll(scrollState)
                    .size(ExplanationSize.imageSize),
            model = R.drawable.graphic_1,
            contentDescription = null,
        )

        Column(
            modifier =
                Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(largeSize),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(largeSize),
        ) {
            Text(
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
private fun ExplanationButton(onNavigateToUserCreation: () -> Unit) {
    Button(
        modifier =
            Modifier
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
    ExplanationScreen {}
}
