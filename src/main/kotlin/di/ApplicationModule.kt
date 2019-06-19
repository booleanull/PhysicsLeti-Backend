package di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import managers.database.DatabaseManager
import managers.database.IDatabaseManager
import managers.labwork.ILabworkManager
import managers.labwork.LabworkManager
import managers.teach.ITeachManager
import managers.teach.TeachManager
import managers.token.ITokenManager
import managers.token.TokenManager
import managers.user.IUserManager
import managers.user.UserManager
import utils.Constants
import javax.inject.Singleton
import javax.mail.Authenticator
import javax.mail.PasswordAuthentication
import javax.mail.Session

@Module
class ApplicationModule {

    @Singleton
    @Provides
    internal fun provideGson(): Gson {
        return GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .disableHtmlEscaping()
            .setPrettyPrinting()
            .create()
    }

    @Singleton
    @Provides
    internal fun provideEmailSession(): Session {
        val properties = System.getProperties()
        properties.setProperty("mail.smtp.host", Constants.HOST)
        properties.setProperty("mail.smtp.auth", "true")
        properties.setProperty("mail.smtp.starttls.enable", "true")
        properties.setProperty("mail.smtp.port", Constants.PORT)

        return Session.getInstance(properties,
            object : Authenticator() {
                override fun getPasswordAuthentication(): PasswordAuthentication {
                    return PasswordAuthentication(Constants.USER, Constants.PASSWORD)
                }
            })
    }

    @Singleton
    @Provides
    internal fun provideDatabaseManager(): IDatabaseManager {
        return DatabaseManager()
    }

    @Singleton
    @Provides
    internal fun provideUserManager(): IUserManager {
        return UserManager()
    }

    @Singleton
    @Provides
    internal fun provideTokenManager(): ITokenManager {
        return TokenManager()
    }

    @Singleton
    @Provides
    internal fun provideLabworkManager(): ILabworkManager {
        return LabworkManager()
    }

    @Singleton
    @Provides
    internal fun provideTeachManager(): ITeachManager {
        return TeachManager()
    }
}