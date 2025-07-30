package merail.otp.password.passwordCreation

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import merail.otp.auth.api.IAuthRepository
import merail.otp.core.extensions.suspendableRunCatching
import merail.otp.navigation.domain.NavigationRoute
import javax.inject.Inject

@HiltViewModel
internal class PasswordCreationViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val authRepository: IAuthRepository,
) : ViewModel() {

    companion object {
        private const val TAG = "PasswordCreationViewModel"
    }

    private val email = savedStateHandle.toRoute<NavigationRoute.PasswordCreation>().email

    private val passwordCreationValidator = PasswordCreationValidator()

    private val _userCreatingState = MutableStateFlow<UserCreatingState>(UserCreatingState.None)

    private val _passwordValueState = MutableStateFlow(PasswordValueState())

    private val _repeatedPasswordValueState = MutableStateFlow(PasswordValueState())

    val userCreatingState = _userCreatingState

    val passwordValueState = _passwordValueState

    val repeatedPasswordValueState = _repeatedPasswordValueState

    fun updatePassword(
        value: String,
    ) {
        _passwordValueState.update {
            it.copy(
                password = value,
                isValid = true,
            )
        }
        _repeatedPasswordValueState.update {
            it.copy(
                isValid = true,
            )
        }
    }

    fun updateRepeatedPassword(
        value: String,
    ) {
        _repeatedPasswordValueState.update {
           it.copy(
               password = value,
               isValid = true,
           )
        }
    }

    fun validate() {
        val isPasswordValid = passwordCreationValidator(_passwordValueState.value.password)
        val isRepeatedPasswordValid = _passwordValueState.value == _repeatedPasswordValueState.value
        _passwordValueState.update {
            it.copy(
                isValid = isPasswordValid,
            )
        }
        _repeatedPasswordValueState.update {
            it.copy(
                isValid = isRepeatedPasswordValid,
            )
        }
        if (isPasswordValid && isRepeatedPasswordValid) {
            createUser()
        }
    }

    private fun createUser() {
        viewModelScope.launch {
            _userCreatingState.update {
                UserCreatingState.Loading
            }
            suspendableRunCatching {
                Log.d(TAG, "User creation. Start")
                authRepository.createUser(
                    email = email,
                    password = _passwordValueState.value.password,
                )
            }.onFailure { throwable ->
                Log.w(TAG, "User creation. Failure", throwable)
                _userCreatingState.update {
                    UserCreatingState.Error(throwable.cause)
                }
            }.onSuccess {
                Log.d(TAG, "User creation. Success")
                _userCreatingState.update {
                    UserCreatingState.Success
                }
            }
        }
    }
}

