package merail.otp.password.passwordCreation

internal data class PasswordValueState(
    val password: String = "",
    val isValid: Boolean = true,
)

internal class PasswordCreationValidator {

    private val passwordRegex = Regex(
        pattern = "^(.{0,7}|[^0-9]*|[^A-Z]*|[^a-z]*|[a-zA-Z0-9]*)\$",
        option = RegexOption.IGNORE_CASE,
    )

    operator fun invoke(password: String) = passwordRegex.matches(password).not()
}

internal class PasswordAuthValidator {
    operator fun invoke(value: String) = value.isNotBlank()
}