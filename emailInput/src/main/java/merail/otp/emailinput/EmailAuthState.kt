package merail.otp.emailinput

internal sealed class EmailAuthState {

    data object None : EmailAuthState()

    data object Loading: EmailAuthState()

    data class Error(val exception: Throwable?): EmailAuthState()

    data object OtpWasSent: EmailAuthState()

    data object UserExists: EmailAuthState()
}

internal val EmailAuthState.needToBlockUi
    get() = this is EmailAuthState.Loading
            || this is EmailAuthState.UserExists
            || this is EmailAuthState.OtpWasSent