package managers.token

import controllers.auth.models.AuthData

interface ITokenManager {

    /**
     * Method generates unique auth token using [authData]
     *
     * @return generated auth token with encoded [authData]
     */
    fun generateAuthToken(authData: AuthData): String

    /**
     * Method validates that auth [token] exist
     *
     * @return true if [token] exists false elsewhere
     */
    fun validateAuthToken(token: String): Boolean
}