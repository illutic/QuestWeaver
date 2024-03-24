package g.sig.questweaver

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import g.sig.questweaver.navigation.AppNavHost
import g.sig.ui.AppTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            navController = rememberNavController()

            AppTheme {
                AppNavHost(
                    modifier = Modifier.fillMaxSize(),
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
