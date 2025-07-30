package merail.otp.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import merail.otp.design.OtpSystemTheme
import merail.otp.design.styles.ButtonStyle

@Composable
fun HomeContainer(
    onExit: () -> Unit,
) = HomeScreen(onExit)

@Composable
internal fun HomeScreen(
    onExit: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel<HomeViewModel>(),
) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .systemBarsPadding()
            .fillMaxSize(),
    ) {
        Text(
            text = stringResource(R.string.home_title),
            style = OtpSystemTheme.typography.displayMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth(),
        )

        Button(
            onClick = {
                viewModel.signOut()
                onExit()
            },
            colors = ButtonStyle.Primary.colors(),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .padding(
                    horizontal = 12.dp,
                    vertical = 64.dp,
                )
                .fillMaxWidth()
                .height(64.dp),
        ) {
            Text(
                text = stringResource(R.string.home_exit_button),
                textAlign = TextAlign.Center,
                style = OtpSystemTheme.typography.titleMedium,
            )
        }
    }
}