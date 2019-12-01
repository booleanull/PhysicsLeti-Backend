package controllers.auth

import com.google.gson.Gson
import controllers.auth.models.AuthData
import controllers.auth.models.RegisterData
import controllers.auth.responses.error.UserAlreadyExistsErrorResponse
import controllers.auth.responses.error.WrongRegisterDataErrorResponse
import controllers.auth.responses.error.WrongUserDataErrorResponse
import controllers.auth.responses.ok.UserAuthorisationOkResponse
import controllers.auth.responses.ok.UserRegisteredOkResponse
import controllers.base.BaseController
import controllers.base.post
import controllers.base.responses.error.UserIsNotApprovedErrorResponse
import daggerApplicationComponent
import managers.database.base.toHibUser
import managers.token.ITokenManager
import managers.user.IUserManager
import org.apache.commons.codec.binary.Hex
import org.apache.commons.codec.digest.DigestUtils
import utils.fromJson
import javax.inject.Inject

class AuthController : BaseController {

    @Inject
    lateinit var gson: Gson
    @Inject
    lateinit var userManager: IUserManager
    @Inject
    lateinit var tokenManager: ITokenManager

    init {
        daggerApplicationComponent.inject(this)
    }

    override fun start() {
        initUserRegistration()
        initUserAuth()
    }

    private fun initUserAuth() {
        post("/auth", { request, response ->
            val requestAuthData = request.body().fromJson<AuthData>()
            val password = String(Hex.encodeHex(DigestUtils.md5(requestAuthData.password)))
            val authData = AuthData(requestAuthData.email, password)
            val user = userManager.getUser(authData)

            if (user == null) WrongUserDataErrorResponse.halt(gson)
            if (!user!!.approve) UserIsNotApprovedErrorResponse.halt(gson)

            val hibUser = user.toHibUser()
            if (hibUser.restored) {
                val token = tokenManager.generateAuthToken(authData)
                hibUser.token = token
                hibUser.restored = false
                userManager.saveUser(hibUser)
            }
            UserAuthorisationOkResponse("${hibUser.firstName} ${hibUser.lastName}", hibUser.type, hibUser.token, hibUser.id)
        }, gson::toJson)
    }

    private fun initUserRegistration() {
        post("/register", { request, response ->
            val registerData = request.body().fromJson<RegisterData>()
            val user = registerData.toUser().toHibUser()

            val password = String(Hex.encodeHex(DigestUtils.md5(user.password)))
            val authData = AuthData(user.email, password)
            val token = tokenManager.generateAuthToken(authData)
            user.password = password
            user.token = token

            if (user.type == 2) user.approve = true

            if (user.type == 0 && user.groupNumber == 0) WrongRegisterDataErrorResponse.halt(gson)
            if (user.type == 1 && user.groupNumber != 0) WrongRegisterDataErrorResponse.halt(gson)
            //if(user.type != 1 && user.type != 0) WrongRegisterDataErrorResponse.halt(gson)

            val id = userManager.createUser(user)
            if (id == -1) UserAlreadyExistsErrorResponse.halt(gson)
            UserRegisteredOkResponse("${user.firstName} ${user.lastName}", user.type, user.token)
        }, gson::toJson)
    }
}