package merail.otp.otpinput

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import merail.otp.auth.api.IAuthRepository
import merail.otp.core.extensions.suspendableRunCatching
import merail.otp.navigation.domain.NavigationRoute
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
internal class OtpInputViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val authRepository: IAuthRepository,
) : ViewModel() {

    companion object {
        private const val TAG = "OtpInputViewModel"

        private const val OTP_EXPIRED_TIME = 300L

        private const val OTP_RESEND_TIME = 60L

        private const val OTP_BLOCK_TIME = 300L

        private const val MAX_ATTEMPTS_COUNT = 5
    }

    private val _otpValueState = MutableStateFlow(OtpValueState())

    private val _otpResendState = MutableStateFlow<OtpResendState>(OtpResendState.None)

    private val _otpResendRemindTime = MutableStateFlow(OTP_RESEND_TIME)

    private val _isCountdownTextVisible = MutableStateFlow(false)

    private val otpValidator = OtpValidator()

    private var initialTimestamp = 0L

    private var otpResendCountdownJob: Job? = null

    private var attemptsCount = 0

    val email = savedStateHandle.toRoute<NavigationRoute.OtpInput>().email

    val otpValueState: StateFlow<OtpValueState> = _otpValueState

    val otpResendState: StateFlow<OtpResendState> = _otpResendState

    val otpResendRemindTime: StateFlow<Long> = _otpResendRemindTime

    val isCountdownTextVisible: StateFlow<Boolean> = _isCountdownTextVisible

    init {
        viewModelScope.launch {
            initialTimestamp = System.currentTimeMillis()

            otpResendCountdownJob = startOtpResendCountdown()
        }
    }

    fun updateOtp(value: String) {
        if (otpValidator.validate(value)) {
            _otpValueState.update {
                it.copy(
                    otp = value,
                    isOtpNotExpired = true,
                    isOtpVerified = true,
                    hasAvailableAttempts = true,
                )
            }
        }
    }

    fun verifyOtp(): Boolean {
        val isOtpNotExpired = OTP_EXPIRED_TIME - differenceBetweenCurrentAndInitialTimestamps > 0

        val isOtpVerified = authRepository.getCurrentOtp() == _otpValueState.value.otp

        if (isOtpVerified.not() && isOtpNotExpired) {
            attemptsCount++
        }
        val hasAvailableAttempts = attemptsCount < MAX_ATTEMPTS_COUNT
        if (hasAvailableAttempts.not()) {
            otpResendCountdownJob?.cancel()
            _otpResendRemindTime.update {
                OTP_BLOCK_TIME
            }
            otpResendCountdownJob = startOtpResendCountdown()
        }

        _otpValueState.update {
            it.copy(
                isOtpNotExpired = isOtpNotExpired,
                isOtpVerified = isOtpVerified,
                hasAvailableAttempts = hasAvailableAttempts,
            )
        }

        return isValid
    }

    private fun startOtpResendCountdown() = viewModelScope.launch {
        _isCountdownTextVisible.update {
            true
        }
        while(_otpResendRemindTime.value > 0) {
            delay(1000)
            _otpResendRemindTime.update {
                it - 1
            }
        }
        _isCountdownTextVisible.update {
            false
        }
    }

    fun resendOtp() = viewModelScope.launch {
        suspendableRunCatching {
            Log.d(TAG, "OTP sending. Start")
            _otpResendState.update {
                OtpResendState.Loading
            }
            authRepository.sendOtp(email)
        }.onFailure { throwable ->
            Log.w(TAG, "OTP sending. Failure", throwable)
            _otpResendState.update {
                OtpResendState.Error(throwable)
            }
        }.onSuccess {
            Log.d(TAG, "OTP sending. Success")
            _otpResendRemindTime.update {
                OTP_RESEND_TIME
            }
            initialTimestamp = System.currentTimeMillis()
            attemptsCount = 0
            _otpValueState.update {
                it.copy(
                    otp = "",
                    isOtpNotExpired = true,
                    isOtpVerified = true,
                    hasAvailableAttempts = true,
                )
            }
            otpResendCountdownJob = startOtpResendCountdown()
            _otpResendState.update {
                OtpResendState.OtpWasResent
            }
        }
    }

    private val differenceBetweenCurrentAndInitialTimestamps: Long
        get() = TimeUnit.MILLISECONDS.toSeconds(
            System.currentTimeMillis() - initialTimestamp,
        )
}

internal val OtpInputViewModel.isValid: Boolean
    get() = otpValueState.value.isValid

internal val OtpInputViewModel.isInputAvailable: Boolean
    get() = otpValueState.value.isInputAvailable

internal val OtpInputViewModel.isOtpResendingAvailable: Boolean
    get() = isCountdownTextVisible.value.not() && otpResendState.value.needToBlockUi.not()

