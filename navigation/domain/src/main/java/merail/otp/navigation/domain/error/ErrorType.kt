package merail.otp.navigation.domain.error

import merail.otp.core.exceptions.NoInternetConnectionException

enum class ErrorType {
    INTERNET_CONNECTION,
    OTHER,
    ;
}

fun Throwable?.toType() = when {
    this as? NoInternetConnectionException != null -> ErrorType.INTERNET_CONNECTION
    else -> ErrorType.OTHER
}