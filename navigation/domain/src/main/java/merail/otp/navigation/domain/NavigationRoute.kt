package merail.otp.navigation.domain

import kotlinx.serialization.Serializable
import merail.otp.navigation.domain.error.ErrorType

sealed class NavigationRoute {
    @Serializable
    data object EmailInput : NavigationRoute()

    @Serializable
    data class PasswordEnter(
        val email: String,
    ) : NavigationRoute()

    @Serializable
    data class OtpInput(
        val email: String,
    ) : NavigationRoute()

    @Serializable
    data class PasswordCreation(
        val email: String,
    ) : NavigationRoute()

    @Serializable
    data class Error(
        val errorType: ErrorType,
    ) : NavigationRoute() {
        companion object {
            const val ERROR_TYPE_KEY = "errorType"
        }
    }
}