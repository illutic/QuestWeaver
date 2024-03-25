package g.sig.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import g.sig.home.screens.HomeScreen

fun NavGraphBuilder.homeGraph(
    onNavigateToProfile: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToHostGame: () -> Unit,
    onNavigateToJoinGame: () -> Unit,
    onNavigateToPermissions: () -> Unit,
    onNavigateToGame: (String) -> Unit,
) {
    composable(HomeRoute.path) {
        HomeScreen(
            onNavigateToProfile = onNavigateToProfile,
            onNavigateToSettings = onNavigateToSettings,
            onNavigateToHostGame = onNavigateToHostGame,
            onNavigateToJoinGame = onNavigateToJoinGame,
            onNavigateToPermissions = onNavigateToPermissions,
            onNavigateToGame = onNavigateToGame,
        )
    }
}