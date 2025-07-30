package merail.otp.email

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import merail.otp.auth.api.IAuthRepository
import merail.otp.core.extensions.suspendableRunCatching
import javax.inject.Inject

@HiltViewModel
internal class EmailInputViewModel @Inject constructor(
    private val authRepository: IAuthRepository,
) : ViewModel() {

    companion object {
        private const val TAG = "EmailInputViewModel"
    }

    private val _emailAuthState = MutableStateFlow<EmailAuthState>(EmailAuthState.None)

    private val _emailValueState = MutableStateFlow(EmailValueState())

    private val emailValidator = EmailValidator()

    val emailAuthState: StateFlow<EmailAuthState> = _emailAuthState

    val emailValueState: StateFlow<EmailValueState> = _emailValueState

    fun updateEmail(value: String) {
        _emailValueState.update {
            it.copy(
                email = value,
                isValid = true,
            )
        }
    }

    fun validateEmail() {
        val isEmailValid = emailValidator.validate(_emailValueState.value.email)
        _emailValueState.update {
            it.copy(
                isValid = isEmailValid,
            )
        }
        if (isEmailValid) {
            checkIfUserExist()
        }
    }

    fun resetState() {
        _emailAuthState.update {
            EmailAuthState.None
        }
    }

    private fun checkIfUserExist() = viewModelScope.launch {
        suspendableRunCatching {
            Log.d(TAG, "User ${_emailValueState.value.email} existing check. Start")

            _emailAuthState.update {
                EmailAuthState.Loading
            }

            authRepository.isUserExist(_emailValueState.value.email)
        }.onFailure { throwable ->
            Log.w(TAG, "User ${_emailValueState.value.email} existing check. Failure", throwable)

            _emailAuthState.update {
                EmailAuthState.Error(throwable)
            }
        }.onSuccess {
            Log.d(TAG, "User ${_emailValueState.value.email} existing check. Success ($it)")

            if (it) {
                _emailAuthState.update {
                    EmailAuthState.UserExists
                }
            } else {
                sendOtp()
            }
        }
    }

    private fun sendOtp() = viewModelScope.launch {
        suspendableRunCatching {
            Log.d(TAG, "OTP sending. Start")

            authRepository.sendOtp(_emailValueState.value.email)
        }.onFailure { throwable ->
            Log.w(TAG, "OTP sending. Failure", throwable)

            _emailAuthState.update {
                EmailAuthState.Error(throwable)
            }
        }.onSuccess {
            Log.d(TAG, "OTP sending. Success")

            _emailAuthState.update {
                EmailAuthState.OtpWasSent
            }
        }
    }
}

