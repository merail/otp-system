package merail.otp.auth.impl.mail

import merail.otp.auth.impl.BuildConfig
import javax.mail.Authenticator
import javax.mail.PasswordAuthentication
import javax.mail.Session
import kotlin.reflect.KProperty

internal class EmailSessionCreator {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): Session {
        val properties = System.getProperties()

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

        val session = Session.getInstance(
            properties,
            object : Authenticator() {
                override fun getPasswordAuthentication() = PasswordAuthentication(
                    BuildConfig.HOST_EMAIL,
                    BuildConfig.HOST_PASSWORD,
                )
            },
        )

        return session
    }
}