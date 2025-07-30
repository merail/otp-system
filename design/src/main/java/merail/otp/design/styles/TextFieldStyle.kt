package merail.otp.design.styles

import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import merail.otp.design.OtpTheme

@Immutable
sealed class TextFieldStyle {

    @Composable
    abstract fun colors(): TextFieldColors

    data object Primary : TextFieldStyle() {
        @Composable
        override fun colors(): TextFieldColors = TextFieldDefaults.colors(
            focusedTextColor = OtpTheme.colors.textInversePrimary,
            unfocusedTextColor = OtpTheme.colors.textInversePrimary,
            unfocusedContainerColor = OtpTheme.colors.graphicPrimary,
            focusedContainerColor = OtpTheme.colors.graphicPrimary,
            cursorColor = OtpTheme.colors.textInversePrimary,
            focusedLabelColor = OtpTheme.colors.textInversePrimary,
            selectionColors = TextSelectionColors(
                backgroundColor = OtpTheme.colors.graphicInverseSecondary,
                handleColor = OtpTheme.colors.textInversePrimary,
            ),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedLeadingIconColor = OtpTheme.colors.graphicInversePrimary,
            unfocusedLeadingIconColor = OtpTheme.colors.graphicInversePrimary,
            unfocusedTrailingIconColor = OtpTheme.colors.graphicInversePrimary,
            focusedTrailingIconColor = OtpTheme.colors.graphicInversePrimary,
            unfocusedLabelColor = OtpTheme.colors.textInversePrimary,
            disabledLabelColor = OtpTheme.colors.textInversePrimary,
            errorContainerColor = OtpTheme.colors.graphicNegativeSecondary,
            errorLeadingIconColor = OtpTheme.colors.graphicNegativePrimary,
            errorTextColor = OtpTheme.colors.textNegative,
            errorIndicatorColor = Color.Transparent,
            errorCursorColor = OtpTheme.colors.graphicNegativePrimary,
            errorLabelColor = OtpTheme.colors.textNegative,
            errorTrailingIconColor = OtpTheme.colors.graphicNegativePrimary,
        )
    }
}
