package merail.otp.core.extensions

import java.util.Locale
import kotlin.text.format

fun Long.toCountdownTime(): String {
    val remainingMinutes = this / 60
    val remainingSeconds = this % 60
    return String.format(
        locale = Locale.Builder().setLanguage("ru").build(),
        format = "%02d:%02d",
        remainingMinutes,
        remainingSeconds,
    )
}