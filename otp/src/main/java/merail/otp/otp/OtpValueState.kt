package merail.otp.otp

import kotlin.text.isDigit
import kotlin.text.isEmpty
import kotlin.text.last

internal data class OtpValueState(
    val otp: String = "",
    val isOtpNotExpired: Boolean = true,
    val isOtpVerified: Boolean = true,
    val hasAvailableAttempts: Boolean = true,
) {
    val isValid = isOtpNotExpired && isOtpVerified && hasAvailableAttempts

    val isInputAvailable = isOtpNotExpired && hasAvailableAttempts
}

internal class OtpValidator {
    fun validate(value: String) = value.isEmpty() || value.last().isDigit()
}