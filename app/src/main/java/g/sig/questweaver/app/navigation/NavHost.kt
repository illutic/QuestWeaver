package g.sig.questweaver.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import g.sig.questweaver.game.navigation.GameRoute
import g.sig.questweaver.game.navigation.gameGraph
import g.sig.questweaver.home.navigation.HomeRoute
import g.sig.questweaver.home.navigation.homeGraph
import g.sig.questweaver.hostgame.navigation.HostGameRoute
import g.sig.questweaver.hostgame.navigation.hostGameGraph
import g.sig.questweaver.joingame.navigation.JoinGameRoute
import g.sig.questweaver.joingame.navigation.joinGameGraph
import g.sig.questweaver.onboarding.navigation.OnboardingRoute
import g.sig.questweaver.onboarding.navigation.onboardingGraph
import g.sig.questweaver.permissions.navigation.PermissionRoute
import g.sig.questweaver.permissions.navigation.permissionGraph
import g.sig.questweaver.settings.SettingsRoute
import g.sig.questweaver.settings.settingsGraph
import g.sig.questweaver.ui.SharedTransitionsLayout
import g.sig.questweaver.ui.defaultNavigationEnterTransition
import g.sig.questweaver.ui.defaultNavigationExitTransition
import g.sig.questweaver.user.navigation.UserRoute
import g.sig.questweaver.user.navigation.userGraph

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    SharedTransitionsLayout {
        NavHost(
            modifier = modifier,
            navController = navController,
            startDestination = HomeRoute.path,
            enterTransition = { defaultNavigationEnterTransition },
            exitTransition = { defaultNavigationExitTransition },
        ) {
            appGraph(navController)
        }
    }
}

@Suppress("LongMethod")
private fun NavGraphBuilder.appGraph(navController: NavHostController) {
    onboardingGraph(
        onNavigateToUserCreation = {
            navController.navigate(UserRoute.path)
        },
    )

    userGraph(
        onBack = { navController.popBackStack() },
        onUserSaved = {
            navController.navigate(HomeRoute.path) {
                launchSingleTop = true
                popUpTo(navController.graph.id) { inclusive = true }
            }
        },
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
        onNavigateToQueue = {
            navController.navigate(HostGameRoute.createPath(it)) {
                launchSingleTop = true
            }
        },
        onNavigateToGame = {
            navController.navigate(GameRoute.createPath(it)) {
                launchSingleTop = true
                popUpTo(navController.graph.id) { inclusive = true }
            }
        },
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
        },
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
        },
    )

    gameGraph {
        navController.navigate(HomeRoute.path) {
            launchSingleTop = true
            popUpTo(navController.graph.id) { inclusive = true }
        }
    }
}
