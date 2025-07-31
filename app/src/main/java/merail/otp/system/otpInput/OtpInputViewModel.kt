package merail.otp.system.otpInput

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import merail.otp.system.repository.RegistrationRepository

internal class OtpInputViewModel: ViewModel() {

    val otpValueState = MutableStateFlow(OtpValueState())

    fun updateOtp(
        value: String,
    ) {
        if (validateOtp(value)) {
            otpValueState.value = otpValueState.value.copy(
                otp = value,
                isVerified = true,
            )
        }
    }

    fun verifyOtp(): Boolean {
        val isOtpVerified = RegistrationRepository.getCurrentOtp() == otpValueState.value.otp

        otpValueState.value = otpValueState.value.copy(
            isVerified = isOtpVerified,
        )

        return isOtpVerified
    }

    private fun validateOtp(otp: String) = otp.isEmpty() || otp.last().isDigit()
}

internal data class OtpValueState(
    val otp: String = "",
    val isVerified: Boolean = true,
)