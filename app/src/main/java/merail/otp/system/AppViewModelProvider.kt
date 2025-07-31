package merail.otp.system

import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import merail.otp.system.emailInput.EmailInputViewModel
import merail.otp.system.otpInput.OtpInputViewModel

internal object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            EmailInputViewModel()
        }

        initializer {
            OtpInputViewModel()
        }
    }
}
