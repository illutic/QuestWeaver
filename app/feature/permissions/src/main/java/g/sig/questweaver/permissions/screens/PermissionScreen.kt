package g.sig.questweaver.permissions.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.PermissionState
import g.sig.questweaver.common.ui.components.ImageWithPlaceholder
import g.sig.questweaver.common.ui.layouts.ScreenScaffold
import g.sig.questweaver.permissions.R
import g.sig.questweaver.ui.AppIcons
import g.sig.questweaver.ui.largeSize

@OptIn(ExperimentalPermissionsApi::class)
@Composable
internal fun PermissionScreen(
    permissionsState: MultiplePermissionsState,
    userDeniedPermission: Boolean = false,
    onBack: () -> Unit
) {
    ScreenScaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { PermissionTopBar(onBack) },
        navigation = {
            val context = LocalContext.current
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = largeSize),
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
    ) {
        val scrollState = rememberScrollState()

        ImageWithPlaceholder(
            modifier = Modifier
                .verticalScroll(scrollState)
                .size(PermissionSize.imageSize),
            model = if (userDeniedPermission) {
                R.drawable.graphic_5
            } else {
                R.drawable.graphic_4
            },
            contentDescription = ""
        )

        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .padding(largeSize),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(largeSize)
        ) {
            Text(
                text = if (userDeniedPermission) {
                    stringResource(id = R.string.permission_title_denied)
                } else {
                    stringResource(id = R.string.permission_title)
                },
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = stringResource(id = R.string.permission_header_1),
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = stringResource(id = R.string.permission_body_1),
                style = MaterialTheme.typography.bodyMedium
            )
            HorizontalDivider()
            Text(
                text = stringResource(id = R.string.permission_header_2),
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = stringResource(id = R.string.permission_body_2),
                style = MaterialTheme.typography.bodyMedium
            )
            HorizontalDivider()
        }
    }
}

private fun Context.launchSystemSettings() {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    val uri = Uri.fromParts("package", packageName, null)
    intent.data = uri
    try {
        startActivity(intent)
    } catch (_: Exception) {
        intent.action = Settings.ACTION_MANAGE_ALL_APPLICATIONS_SETTINGS
        startActivity(intent)
    } catch (_: Exception) {
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
    val context = LocalContext.current
    PermissionScreen(
        permissionsState = object : MultiplePermissionsState {
            override val allPermissionsGranted: Boolean = false
            override val permissions: List<PermissionState> = emptyList()
            override val revokedPermissions: List<PermissionState> = emptyList()
            override val shouldShowRationale: Boolean = true
            override fun launchMultiplePermissionRequest() {
                Toast.makeText(context, "permissions", LENGTH_LONG).show()
            }
        },
        onBack = {}
    )
}
