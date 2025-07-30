package merail.otp.auth.impl.di

import android.content.Context
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import merail.otp.auth.api.IAuthRepository
import merail.otp.auth.impl.mail.EmailSender
import merail.otp.auth.impl.repository.AuthRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface AuthModule {
    @Singleton
    @Binds
    fun bindAuthRepository(
        authRepository: AuthRepository,
    ): IAuthRepository

    companion object {
        @Provides
        @Singleton
        fun provideFirebaseAuth(): FirebaseAuth = Firebase.auth

        @Provides
        @Singleton
        fun provideEmailSender(
            @ApplicationContext context: Context,
        ): EmailSender = EmailSender(context)
    }
}