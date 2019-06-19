package managers.token

import controllers.auth.models.AuthData
import daggerApplicationComponent
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import managers.user.IUserManager
import utils.Constants
import utils.tryToFalse
import javax.inject.Inject

class TokenManager : ITokenManager {

    @Inject
    lateinit var userManager: IUserManager

    private val currentTime: Long
        get() = System.currentTimeMillis()

    init {
        daggerApplicationComponent.inject(this)
    }

    override fun generateAuthToken(authData: AuthData) = Jwts.builder()
        .claim("email", authData.email)
        .claim("password", authData.password)
        .claim("currentTime", currentTime)
        .signWith(SignatureAlgorithm.HS512, Constants.JWT_SECRET).compact()

    override fun validateAuthToken(token: String): Boolean {
        return tryToFalse {
            val tokenBody = Jwts.parser()
                .setSigningKey(Constants.JWT_SECRET)
                .parseClaimsJws(token)
                .body

            val email = (tokenBody["email"] as? String) ?: return@tryToFalse false
            val password = (tokenBody["password"] as? String) ?: return@tryToFalse false

            val user = userManager.getUser(AuthData(email, password)) ?: return@tryToFalse false
            val userToken = user.token
            return@tryToFalse userToken == token
        }
    }
}