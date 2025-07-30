package merail.otp.design

import androidx.compose.material3.CardColors
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val LocalOtpColors = staticCompositionLocalOf { Colors() }

val Colors.cardColors: CardColors
    get() = CardColors(
        containerColor = elementBackground,
        contentColor = elementPrimary,
        disabledContainerColor = elementBackground,
        disabledContentColor = elementPrimary,
    )

val Colors.materialThemeColors: ColorScheme
    get() = lightColorScheme(
        primary = screenPrimary,
        background = screenBackground,
        onBackground = screenPrimary,
        surface = screenBackground,
    )

@Immutable
data class Colors(
    val screenPrimary: Color = ColorConstants.white,
    val screenBackground: Color = ColorConstants.black,

    val elementPrimary: Color = ColorConstants.white,
    val elementBackground: Color = ColorConstants.black,
    val elementNegative: Color = ColorConstants.merlot,

    val textPrimary: Color = ColorConstants.white,
    val textNegative: Color = ColorConstants.red_70,
    val textInversePrimary: Color = ColorConstants.black,

    val borderPrimary: Color = ColorConstants.white,

    val graphicPrimary: Color = ColorConstants.white,
    val graphicInversePrimary: Color = ColorConstants.black,
    val graphicInverseSecondary: Color = ColorConstants.lightGrey,
    val graphicNegativePrimary: Color = ColorConstants.red_70,
    val graphicNegativeSecondary: Color = ColorConstants.fairPink,
)

internal object ColorConstants {
    val white = Color(0xFFFFFFFF)
    val black = Color(0xFF000000)
    val thunder = Color(0xFF2D2D2D)
    val red_70 = Color(0xB3FF0000)
    val merlot = Color(0xFF862020)
    val lightGrey = Color(0xFFDBDAD7)
    val fairPink = Color(0xFFFFEEEE)
}