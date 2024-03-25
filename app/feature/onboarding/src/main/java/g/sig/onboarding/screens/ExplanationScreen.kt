package g.sig.onboarding.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import coil.compose.AsyncImage
import g.sig.onboarding.R
import g.sig.onboarding.screens.OnboardingSize.imageSize
import g.sig.ui.largeSize

@Composable
internal fun ExplanationScreen(
    onNavigateToUserCreation: () -> Unit
) {
    Scaffold {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(it)
                .padding(largeSize),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(largeSize)
        ) {
            AsyncImage(
                modifier = Modifier.size(imageSize),
                model = R.drawable.graphic_1,
                contentDescription = null,
            )

            Text(
                text = stringResource(id = R.string.explanation_title),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(largeSize)
            ) {
                Text(
                    text = stringResource(id = R.string.explanation_header_1),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Text(
                    text = stringResource(id = R.string.explanation_body_1),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )

                HorizontalDivider()

                Text(
                    text = stringResource(id = R.string.explanation_header_2),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Text(
                    text = stringResource(id = R.string.explanation_body_2),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )

                HorizontalDivider()
            }

            Button(
                onClick = { onNavigateToUserCreation() }
            ) {
                Text(text = stringResource(id = R.string.explanation_cta))
            }
        }
    }

}