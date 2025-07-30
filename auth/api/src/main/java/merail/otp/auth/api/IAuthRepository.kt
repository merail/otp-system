package merail.otp.auth.api

interface IAuthRepository {

    fun isUserAuthorized(): Boolean

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

    suspend fun authorize(
        email: String,
        password: String,
    )

    fun signOut()
}
