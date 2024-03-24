package g.sig.onboarding.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import g.sig.onboarding.R
import g.sig.onboarding.screens.OnboardingSize.mediumSize
import g.sig.ui.largeSize
import g.sig.ui.R as uiR

@Composable
@Preview
internal fun OnboardingScreen(
    onNavigateToExplanation: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .safeDrawingPadding()
            .padding(largeSize),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(mediumSize)
        ) {
            AsyncImage(
                modifier = Modifier.size(OnboardingSize.logoSize),
                model = uiR.drawable.ic_logo,
                contentDescription = null,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
            )

            Text(
                text = stringResource(id = R.string.onboarding_title),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Button(
            onClick = { onNavigateToExplanation() }
        ) {
            Text(text = stringResource(id = R.string.onboarding_button))
        }
    }
}