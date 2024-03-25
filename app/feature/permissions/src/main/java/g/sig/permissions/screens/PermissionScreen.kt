package g.sig.permissions.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.PermissionState
import g.sig.permissions.R
import g.sig.permissions.screens.PermissionSizes.graphicSize
import g.sig.ui.AppIcons
import g.sig.ui.largeSize

@OptIn(ExperimentalPermissionsApi::class)
@Composable
internal fun PermissionScreen(
    permissionsState: MultiplePermissionsState,
    userDeniedPermission: Boolean = false,
    onBack: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { PermissionTopBar(onBack) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(largeSize),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(largeSize)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(largeSize)
            ) {
                AsyncImage(
                    modifier = Modifier.size(graphicSize),
                    model = if (userDeniedPermission) {
                        R.drawable.graphic_5
                    } else {
                        R.drawable.graphic_4
                    },
                    contentDescription = ""
                )

                Text(
                    text = if (userDeniedPermission) {
                        stringResource(id = R.string.permission_title_denied)
                    } else {
                        stringResource(id = R.string.permission_title)
                    },
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.primary
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(largeSize)
                ) {
                    Text(text = stringResource(id = R.string.permission_header_1), style = MaterialTheme.typography.titleLarge)
                    Text(text = stringResource(id = R.string.permission_body_1), style = MaterialTheme.typography.bodyMedium)
                    HorizontalDivider()
                    Text(text = stringResource(id = R.string.permission_header_2), style = MaterialTheme.typography.titleLarge)
                    Text(text = stringResource(id = R.string.permission_body_2), style = MaterialTheme.typography.bodyMedium)
                    HorizontalDivider()
                }
            }

            Box(
                modifier = Modifier.width(IntrinsicSize.Max),
                contentAlignment = Alignment.Center
            ) {
                val context = LocalContext.current
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        if (userDeniedPermission) {
                            context.launchSystemSettings()
                        } else {
                            permissionsState.launchMultiplePermissionRequest()
                        }
                    }
                ) {
                    Text(
                        text = if (userDeniedPermission) {
                            stringResource(id = R.string.permission_cta_denied)
                        } else {
                            stringResource(id = R.string.permission_cta)
                        }
                    )
                }
            }
        }
    }
}

private fun Context.launchSystemSettings() {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    val uri = Uri.fromParts("package", packageName, null)
    intent.data = uri
    try {
        startActivity(intent)
    } catch (e: Exception) {
        intent.action = Settings.ACTION_MANAGE_ALL_APPLICATIONS_SETTINGS
        startActivity(intent)
    } catch (e: Exception) {
        intent.action = Settings.ACTION_SETTINGS
        startActivity(intent)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PermissionTopBar(
    onBack: () -> Unit
) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = { onBack() }) {
                Icon(AppIcons.Back, contentDescription = null)
            }
        },
        title = { Text(stringResource(id = R.string.permission_top_title)) }
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@Preview
@Composable
private fun PermissionScreenPreview() {
    PermissionScreen(
        permissionsState = object : MultiplePermissionsState {
            override val allPermissionsGranted: Boolean = false
            override val permissions: List<PermissionState> = emptyList()
            override val revokedPermissions: List<PermissionState> = emptyList()
            override val shouldShowRationale: Boolean = true
            override fun launchMultiplePermissionRequest() {}
        },
        onBack = {}
    )
}