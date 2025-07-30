package merail.otp.core.exceptions

enum class ErrorType {
    INTERNET_CONNECTION,
    OTHER,
    ;
}

fun Throwable?.toType() = when(this) {
    is NoInternetConnectionException -> ErrorType.INTERNET_CONNECTION
    else -> ErrorType.OTHER
}