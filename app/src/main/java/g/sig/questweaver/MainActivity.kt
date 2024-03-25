package g.sig.questweaver

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import g.sig.questweaver.navigation.AppNavHost
import g.sig.questweaver.state.MainState
import g.sig.ui.AppTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()
    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            navController = rememberNavController()
            val state by viewModel.state.collectAsState()
            val startDestination by remember(state) { derivedStateOf { (state as? MainState.Loaded)?.startDestination } }

            AppTheme {
                AppNavHost(
                    modifier = Modifier,
                    startDestination = startDestination,
                    navController
                )
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (!::navController.isInitialized) return

        intent?.data?.let {
            navController.graph.forEach { destination ->
                if (destination.hasDeepLink(it)) {
                    navController.navigate(it)
                    return
                }
            }
        }
    }
}
