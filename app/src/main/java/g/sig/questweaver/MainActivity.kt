package g.sig.questweaver

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import g.sig.domain.repositories.PayloadRepository
import g.sig.questweaver.navigation.AppNavHost
import g.sig.ui.AppTheme
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var navController: NavHostController

    @Inject
    lateinit var payloadRepository: PayloadRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        lifecycleScope.launch {
            payloadRepository.data.collect {
                Log.d("MainActivity", "PayloadData: $it")
            }
        }

        setContent {
            navController = rememberNavController()
            AppTheme {
                AppNavHost(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background),
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
