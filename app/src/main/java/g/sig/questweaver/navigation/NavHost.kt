package g.sig.questweaver.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import g.sig.game.navigation.GameRoute
import g.sig.game.navigation.gameGraph
import g.sig.home.navigation.HomeRoute
import g.sig.home.navigation.homeGraph
import g.sig.host_game.navigation.HostGameRoute
import g.sig.host_game.navigation.hostGameGraph
import g.sig.join_game.navigation.JoinGameRoute
import g.sig.join_game.navigation.joinGameGraph
import g.sig.onboarding.navigation.OnboardingRoute
import g.sig.onboarding.navigation.onboardingGraph
import g.sig.permissions.navigation.PermissionRoute
import g.sig.permissions.navigation.permissionGraph
import g.sig.settings.SettingsRoute
import g.sig.settings.settingsGraph
import g.sig.user.navigation.UserRoute
import g.sig.user.navigation.userGraph

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = HomeRoute.path
    ) {
        onboardingGraph(
            onNavigateToUserCreation = {
                navController.navigate(UserRoute.path)
            }
        )

        userGraph(
            onBack = { navController.popBackStack() },
            onUserSaved = {
                navController.navigate(HomeRoute.path) {
                    launchSingleTop = true
                    popUpTo(navController.graph.id) { inclusive = true }
                }
            }
        )

        homeGraph(
            onNavigateToOnboarding = {
                navController.navigate(OnboardingRoute.path) {
                    popUpTo(HomeRoute.path) { inclusive = true }
                }
            },
            onNavigateToProfile = {
                navController.navigate(UserRoute.path)
            },
            onNavigateToSettings = {
                navController.navigate(SettingsRoute.path)
            },
            onNavigateToHostGame = {
                navController.navigate(HostGameRoute.path)
            },
            onNavigateToJoinGame = {
                navController.navigate(JoinGameRoute.path)
            },
            onNavigateToPermissions = {
                navController.navigate(PermissionRoute.path)
            },
            onNavigateToGame = {}
        )

        permissionGraph(navController) {
            navController.popBackStack()
        }

        settingsGraph { navController.popBackStack() }

        joinGameGraph(
            onBack = { navController.popBackStack() },
            onNavigateToPermissions = {
                navController.navigate(PermissionRoute.path)
            },
            onNavigateToGame = { id ->
                navController.navigate(GameRoute.createPath(id)) {
                    launchSingleTop = true
                    popUpTo(navController.graph.id) { inclusive = true }
                }
            }
        )

        hostGameGraph(
            onBack = { navController.popBackStack() },
            onNavigateToPermissions = {
                navController.navigate(PermissionRoute.path)
            },
            onNavigateToQueue = {
                navController.navigate(HostGameRoute.QUEUE_PATH)
            },
            onNavigateHome = {
                navController.popBackStack()
            },
            onGameCreated = { id ->
                navController.navigate(GameRoute.createPath(id)) {
                    launchSingleTop = true
                    popUpTo(navController.graph.id) { inclusive = true }
                }
            }
        )

        gameGraph()
    }
}