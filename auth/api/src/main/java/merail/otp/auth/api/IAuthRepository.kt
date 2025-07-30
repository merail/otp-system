package merail.otp.auth.api

interface IAuthRepository {

    suspend fun isUserExist(
        email: String,
    ): Boolean

    suspend fun sendOtp(
        email: String,
    )

    fun getCurrentOtp(): String

    suspend fun createUser(
        email: String,
        password: String,
    )

    suspend fun authorizeWithEmail(
        email: String,
        password: String,
    )
}
