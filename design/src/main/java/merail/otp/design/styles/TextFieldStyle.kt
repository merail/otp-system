package merail.otp.design.styles

import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import merail.otp.design.OtpSystemTheme

@Immutable
sealed class TextFieldStyle {

    @Composable
    abstract fun colors(): TextFieldColors

    data object Primary : TextFieldStyle() {
        @Composable
        override fun colors(): TextFieldColors = TextFieldDefaults.colors(
            focusedTextColor = OtpSystemTheme.colors.textInversePrimary,
            unfocusedTextColor = OtpSystemTheme.colors.textInversePrimary,
            unfocusedContainerColor = OtpSystemTheme.colors.graphicPrimary,
            focusedContainerColor = OtpSystemTheme.colors.graphicPrimary,
            cursorColor = OtpSystemTheme.colors.textInversePrimary,
            focusedLabelColor = OtpSystemTheme.colors.textInversePrimary,
            selectionColors = TextSelectionColors(
                backgroundColor = OtpSystemTheme.colors.graphicInverseSecondary,
                handleColor = OtpSystemTheme.colors.textInversePrimary,
            ),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedLeadingIconColor = OtpSystemTheme.colors.graphicInversePrimary,
            unfocusedLeadingIconColor = OtpSystemTheme.colors.graphicInversePrimary,
            unfocusedTrailingIconColor = OtpSystemTheme.colors.graphicInversePrimary,
            focusedTrailingIconColor = OtpSystemTheme.colors.graphicInversePrimary,
            unfocusedLabelColor = OtpSystemTheme.colors.textInversePrimary,
            disabledLabelColor = OtpSystemTheme.colors.textInversePrimary,
            errorContainerColor = OtpSystemTheme.colors.graphicNegativeSecondary,
            errorLeadingIconColor = OtpSystemTheme.colors.graphicNegativePrimary,
            errorTextColor = OtpSystemTheme.colors.textNegative,
            errorIndicatorColor = Color.Transparent,
            errorCursorColor = OtpSystemTheme.colors.graphicNegativePrimary,
            errorLabelColor = OtpSystemTheme.colors.textNegative,
            errorTrailingIconColor = OtpSystemTheme.colors.graphicNegativePrimary,
        )
    }
}
