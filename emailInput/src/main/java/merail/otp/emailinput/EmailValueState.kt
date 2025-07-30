package merail.otp.emailinput

internal data class EmailValueState(
    val email: String = "",
    val isValid: Boolean = true,
)

internal class EmailValidator {

    private val emailRegex = Regex(
        pattern = "^[A-Z0-9._%!+$]+@[A-Z0-9.]+\\.[A-Z]{2,}\$",
        option = RegexOption.IGNORE_CASE,
    )

    fun validate(value: String) = emailRegex.matches(value)
}