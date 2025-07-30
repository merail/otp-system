package merail.otp.design.styles

import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import merail.otp.design.ColorConstants
import merail.otp.design.OtpSystemTheme

@Immutable
sealed class ButtonStyle {

    @Composable
    abstract fun colors(): ButtonColors

    data object Primary : ButtonStyle() {
        @Composable
        override fun colors() = ButtonDefaults.buttonColors(
            containerColor = ColorConstants.thunder,
            contentColor = OtpSystemTheme.colors.textPrimary,
            disabledContainerColor = ColorConstants.thunder,
            disabledContentColor = OtpSystemTheme.colors.textPrimary,
        )
    }
}
