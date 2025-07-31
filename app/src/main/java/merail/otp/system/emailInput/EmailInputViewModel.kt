package merail.otp.system.emailInput

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import merail.otp.system.repository.RegistrationRepository

internal class EmailInputViewModel : ViewModel() {

    val emailState = MutableStateFlow(EmailState(""))

    val sendOtpState = MutableStateFlow<SendOtpState>(SendOtpState.None)

    private val emailRegex = Regex(
        pattern = "^[A-Z0-9._%!+\\-]+@[A-Z0-9.\\-]+\\.[A-Z]{2,}\$",
        option = RegexOption.IGNORE_CASE,
    )

    fun updateEmail(
        value: String,
    ) {
        emailState.value = emailState.value.copy(
            email = value,
            isValid = true,
        )
    }

    fun validateEmail() {
        val isEmailValid = emailRegex.matches(emailState.value.email)

        emailState.value = emailState.value.copy(
            isValid = isEmailValid,
        )

        if (isEmailValid) {
            sendOtp()
        }
    }

    fun updateSendOtpState(sendOtpState: SendOtpState) {
        this.sendOtpState.value = sendOtpState
    }

    private fun sendOtp() = viewModelScope.launch {
        try {
            sendOtpState.value = SendOtpState.Loading

            RegistrationRepository.sendOtp(emailState.value.email)

            sendOtpState.value = SendOtpState.Success
        } catch (cancellationException: CancellationException) {
            throw cancellationException
        } catch (exception: Exception) {
            exception.printStackTrace()

            sendOtpState.value = SendOtpState.Error
        }
    }
}

internal data class EmailState(
    val email: String = "",
    val isValid: Boolean = true,
)

internal sealed class SendOtpState {
    data object None : SendOtpState()

    data object Loading: SendOtpState()

    data object Error : SendOtpState()

    data object Success: SendOtpState()
}

