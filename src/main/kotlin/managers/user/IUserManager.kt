package managers.user

import controllers.auth.models.AuthData
import managers.database.dao.models.HibUser
import managers.user.models.User

interface IUserManager {

    /**
     * Method creates [user] to database and sends its id(that also set in [user]) or -1(error case, id not set in [user])
     *
     * @return id of user in db or -1 (error)
     */
    fun createUser(user: HibUser): Int

    /**
     * Method tries to find user by [id] in database
     *
     * @return founded user or null
     */
    fun getUser(id: Int): User?

    /**
     * Method tries to find user by [AuthData] in database
     *
     * @return founded user or null
     */
    fun getUser(authData: AuthData): User?

    /**
     * Method tries to find user by [token] in database
     *
     * @return founded user or null
     */
    fun getUserByToken(token: String): User?

    /**
     * Method tries to find user by [email] in database
     *
     * @return founded user or null
     */
    fun getUserByEmail(email: String): User?

    /**
     * Method saves [user] of User to database and sends its updated model or null
     *
     * @return updated user or null(error)
     */
    fun saveUser(user: HibUser): User?

    /**
     * Method delete [user] of User in database
     */
    fun removeUser(user: HibUser)

    /**
     * Method get not confirmed users
     *
     * @return list of [User]
     */
    fun getListOfNotConfirmed(): List<User>
}