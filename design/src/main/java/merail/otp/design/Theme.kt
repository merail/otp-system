package merail.otp.design

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember

@Composable
fun OtpTheme(content: @Composable () -> Unit) {
    val colors = remember {
        Colors()
    }

    val typography = remember {
        Typography
    }

    MaterialTheme(
        colorScheme = colors.materialThemeColors,
        typography = typography.materialTypography,
    ) {
        content()
    }
}

object OtpTheme {
    val colors: Colors
        @Composable
        @ReadOnlyComposable
        get() = LocalOtpColors.current

    val typography: Typography
        @Composable
        @ReadOnlyComposable
        get() = LocalTypography.current
}