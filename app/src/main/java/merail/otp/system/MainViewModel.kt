package merail.otp.system

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import merail.otp.auth.api.IAuthRepository
import javax.inject.Inject

@HiltViewModel
internal class MainViewModel @Inject constructor(
    authRepository: IAuthRepository,
): ViewModel() {

    val isUserAuthorized = authRepository.isUserAuthorized()
}