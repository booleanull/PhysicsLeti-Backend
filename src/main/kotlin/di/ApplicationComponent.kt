package di

import controllers.accept.AcceptController
import controllers.auth.AuthController
import controllers.labwork.LabworkController
import controllers.madelabwork.MadeLabworkController
import controllers.reset.ResetController
import controllers.settings.SettingsController
import controllers.teach.TeachController
import dagger.Component
import managers.database.DatabaseManager
import managers.labwork.LabworkManager
import managers.teach.TeachManager
import managers.token.TokenManager
import managers.user.UserManager
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, DaoModules::class])
interface ApplicationComponent {

    fun inject(authController: AuthController)
    fun inject(resetController: ResetController)
    fun inject(acceptController: AcceptController)
    fun inject(labworkController: LabworkController)
    fun inject(madeLabworkController: MadeLabworkController)
    fun inject(teachController: TeachController)
    fun inject(settingsController: SettingsController)

    fun inject(databaseManager: DatabaseManager)
    fun inject(userManager: UserManager)
    fun inject(tokenManager: TokenManager)
    fun inject(labworkManager: LabworkManager)
    fun inject(teachManager: TeachManager)
}