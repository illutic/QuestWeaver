package g.sig.home.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun HomeScreen(
    onNavigateToProfile: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToHostGame: () -> Unit,
    onNavigateToJoinGame: () -> Unit,
    onNavigateToPermissions: () -> Unit,
    onNavigateToGame: (String) -> Unit,
) {
    Text(text = "Home Screen")
}