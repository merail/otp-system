package merail.otp.system.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage
import kotlin.random.Random

internal object RegistrationRepository {

    private val properties = System.getProperties()

    private val session: Session

    private var code = ""

    init {
        properties["mail.transport.protocol"] = "smtp"
        properties["mail.host"] = "smtp.gmail.com"
        properties["mail.smtp.host"] = "smtp.gmail.com"
        properties["mail.smtp.port"] = "465"
        properties["mail.smtp.socketFactory.fallback"] = "false"
        properties["mail.smtp.quitwait"] = "false"
        properties["mail.smtp.socketFactory.port"] = "465"
        properties["mail.smtp.starttls.enable"] = "true"
        properties["mail.smtp.socketFactory.class"] = "javax.net.ssl.SSLSocketFactory"
        properties["mail.smtp.ssl.enable"] = "true"
        properties["mail.smtp.auth"] = "true"
        properties["mail.smtp.ssl.protocols"] = "TLSv1.2"

        session = Session.getInstance(
            properties,
            object : Authenticator() {
                override fun getPasswordAuthentication(): PasswordAuthentication {
                    return PasswordAuthentication(
                        // YOUR EMAIL HERE
                        "rail1mesherov@gmail.com",
                        // YOUR PASSWORD HERE
                        "zrwt osju llxe nmvi",
                    )
                }
            },
        )
    }

    suspend fun sendOtp(
        email: String,
    ) = withContext(Dispatchers.IO) {
        val mimeMessage = MimeMessage(session)
        mimeMessage.addRecipient(Message.RecipientType.TO, InternetAddress(email))
        mimeMessage.subject = "Confirm your email address"
        code = generateCode()
        mimeMessage.setText("Please confirm your email address. Verification code: $code")
        Transport.send(mimeMessage)
    }

    fun getCurrentOtp() = code

    private fun generateCode(): String {
        val generatedValue = Random.nextInt(
            0,
            10_000,
        )

        code = generatedValue.toString()
        while (code.length < 4) {
            code = "0$code"
        }

        return code
    }
}