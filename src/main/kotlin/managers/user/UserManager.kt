package managers.user

import controllers.auth.models.AuthData
import daggerApplicationComponent
import managers.database.IDatabaseManager
import managers.database.dao.models.HibUser
import managers.user.models.User
import javax.inject.Inject

class UserManager : IUserManager {

    @Inject
    lateinit var databaseManager: IDatabaseManager

    init {
        daggerApplicationComponent.inject(this)
    }

    override fun createUser(user: HibUser): Int {
        return databaseManager.createUser(user)
    }

    override fun getUser(id: Int): User? {
        return databaseManager.getUser(id)
    }

    override fun getUser(authData: AuthData): User? {
        return databaseManager.getUser(authData)
    }

    override fun getUserByToken(token: String): User? {
        return databaseManager.getUserByToken(token)
    }

    override fun getUserByEmail(email: String): User? {
        return databaseManager.getUserByEmail(email)
    }

    override fun saveUser(user: HibUser): User? {
        return databaseManager.saveUser(user)
    }

    override fun removeUser(user: HibUser) {
        databaseManager.removeUser(user)
    }

    override fun getListOfNotConfirmed(): List<User> {
        return databaseManager.getListOfNotConfirmed()
    }
}