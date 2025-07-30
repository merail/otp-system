package merail.otp.password.passwordEnter

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import merail.otp.auth.api.IAuthRepository
import merail.otp.core.exceptions.InvalidCredentialsException
import merail.otp.core.extensions.suspendableRunCatching
import merail.otp.navigation.domain.NavigationRoute
import merail.otp.password.passwordCreation.PasswordAuthValidator
import merail.otp.password.passwordCreation.PasswordValueState
import javax.inject.Inject

@HiltViewModel
internal class PasswordEnterViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val authRepository: IAuthRepository,
) : ViewModel() {

    companion object {
        private const val TAG = "PasswordEnterViewModel"
    }

    private val _authByPasswordState = MutableStateFlow<AuthByPasswordState>(AuthByPasswordState.None)

    private val _passwordValueState = MutableStateFlow(PasswordValueState())

    private val passwordAuthValidator = PasswordAuthValidator()

    val email = savedStateHandle.toRoute<NavigationRoute.PasswordEnter>().email

    val authByPasswordState: StateFlow<AuthByPasswordState> = _authByPasswordState

    val passwordValueState: StateFlow<PasswordValueState> = _passwordValueState

    fun updatePassword(value: String) {
        _passwordValueState.update {
            it.copy(
                password = value,
                isValid = true,
            )
        }
    }

    fun validatePassword() {
        val isPasswordValid = passwordAuthValidator(_passwordValueState.value.password)
        _passwordValueState.update {
            it.copy(
                isValid = isPasswordValid,
            )
        }
        if (isPasswordValid) {
            authorize()
        }
    }

    private fun authorize() {
        viewModelScope.launch {
            _authByPasswordState.update {
                AuthByPasswordState.Loading
            }
            suspendableRunCatching {
                Log.d(TAG, "Password auth. Start")
                authRepository.authorize(
                    email = email,
                    password = _passwordValueState.value.password,
                )
            }.onFailure { throwable ->
                Log.w(TAG, "Password auth. Failure", throwable)
                if (throwable is InvalidCredentialsException) {
                    _passwordValueState.update {
                        it.copy(
                            isValid = false,
                        )
                    }
                    _authByPasswordState.update {
                        AuthByPasswordState.InvalidPassword
                    }
                } else {
                    _authByPasswordState.update {
                        AuthByPasswordState.Error(throwable.cause)
                    }
                }
            }.onSuccess {
                Log.d(TAG, "Password auth. Success")
                _authByPasswordState.update {
                    AuthByPasswordState.Success
                }
            }
        }
    }
}