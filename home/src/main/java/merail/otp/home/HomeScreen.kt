package merail.otp.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import merail.otp.design.OtpSystemTheme

@Composable
fun HomeContainer() = HomeScreen()

@Composable
internal fun HomeScreen() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .systemBarsPadding()
            .fillMaxSize(),
    ) {
        Text(
            text = "You're home",
            style = OtpSystemTheme.typography.displayMedium,
        )
    }
}