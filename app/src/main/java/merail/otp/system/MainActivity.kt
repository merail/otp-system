package merail.otp.system

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import merail.otp.design.OtpSystemTheme
import merail.otp.navigation.graph.OtpSystemNavHost
import kotlin.getValue

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
        )

        super.onCreate(savedInstanceState)

        setContent {
            OtpSystemTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                ) {
                    OtpSystemNavHost(
                        isUserAuthorized = viewModel.isUserAuthorized,
                    )
                }
            }
        }
    }
}