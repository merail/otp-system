package merail.otp.otp

internal sealed class OtpResendState {

    data object None : OtpResendState()

    data object Loading: OtpResendState()

    data class Error(val exception: Throwable?): OtpResendState()

    data object OtpWasResent: OtpResendState()
}

internal val OtpResendState.needToBlockUi
    get() = this is OtpResendState.Loading