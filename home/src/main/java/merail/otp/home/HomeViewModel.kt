package merail.otp.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import merail.otp.auth.api.IAuthRepository
import javax.inject.Inject

@HiltViewModel
internal class HomeViewModel @Inject constructor(
    private val authRepository: IAuthRepository,
) : ViewModel() {

    fun signOut() {
        authRepository.signOut()
    }
}

