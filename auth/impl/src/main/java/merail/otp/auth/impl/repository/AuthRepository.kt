package merail.otp.auth.impl.repository

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.sun.mail.util.MailConnectException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import merail.otp.auth.api.IAuthRepository
import merail.otp.auth.impl.mail.EmailSender
import merail.otp.core.exceptions.NoInternetConnectionException
import javax.inject.Inject

internal class AuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val emailSender: EmailSender,
) : IAuthRepository {

    override fun isUserAuthorized() = firebaseAuth.currentUser?.email.isNullOrBlank().not()

    override suspend fun isUserExist(
        email: String,
    ) = withContext(Dispatchers.IO) {
        try {
            // TODO: Deprecated method. Think about it!
            firebaseAuth.fetchSignInMethodsForEmail(email).await().signInMethods?.isNotEmpty() == true
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            throw if (e is FirebaseNetworkException) {
                NoInternetConnectionException()
            } else {
                e
            }
        }
    }

    override suspend fun sendOtp(email: String) = withContext(Dispatchers.IO) {
        try {
            emailSender.sendOtp(email)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            throw if (e is MailConnectException) {
                NoInternetConnectionException()
            } else {
                e
            }
        }
    }

    override fun getCurrentOtp() = emailSender.getCurrentOtp()

    override suspend fun createUser(
        email: String,
        password: String,
    ) {
        try {
            withContext(Dispatchers.IO) {
                firebaseAuth.createUserWithEmailAndPassword(
                    email,
                    password,
                ).await()
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            throw if (e is FirebaseNetworkException) {
                NoInternetConnectionException()
            } else {
                e
            }
        }
    }

    override suspend fun authorizeWithEmail(
        email: String,
        password: String,
    ) {
        withContext(Dispatchers.IO) {
            try {
                firebaseAuth.signInWithEmailAndPassword(
                    email,
                    password,
                ).await()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                throw if (e is FirebaseNetworkException) {
                    NoInternetConnectionException()
                } else {
                    e
                }
            }
        }
    }
}