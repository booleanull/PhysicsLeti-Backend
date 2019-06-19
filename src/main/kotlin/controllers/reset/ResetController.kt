package controllers.reset

import com.google.gson.Gson
import controllers.auth.models.AuthData
import controllers.auth.models.ForgetData
import controllers.auth.models.ResetData
import controllers.base.BaseController
import controllers.base.post
import controllers.base.responses.BaseOkResponse
import controllers.base.responses.error.TokenNotFoundErrorResponse
import controllers.reset.models.responses.error.WrongEmailErrorResponse
import daggerApplicationComponent
import managers.database.base.toHibUser
import managers.token.ITokenManager
import managers.user.IUserManager
import org.apache.commons.codec.binary.Hex
import org.apache.commons.codec.digest.DigestUtils
import utils.Constants
import utils.fromJson
import javax.inject.Inject
import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class ResetController : BaseController {

    @Inject
    lateinit var gson: Gson
    @Inject
    lateinit var userManager: IUserManager
    @Inject
    lateinit var tokenManager: ITokenManager
    @Inject
    lateinit var emailSession: Session

    init {
        daggerApplicationComponent.inject(this)
    }

    override fun start() {
        initForgetPassword()
        initResetPassword()
    }

    private fun initForgetPassword() {
        post("/forget", { request, response ->
            val forgetData = request.body().fromJson<ForgetData>()

            val user = userManager.getUserByEmail(forgetData.email)
            if (user == null) WrongEmailErrorResponse.halt(gson)

            val hibUser = user!!.toHibUser()
            val token = tokenManager.generateAuthToken(AuthData(user.email, user.password))

            hibUser.token = token
            hibUser.restored = true
            userManager.saveUser(hibUser)

            try {
                val message = MimeMessage(emailSession)
                message.setFrom(InternetAddress(Constants.USER))
                message.addRecipient(Message.RecipientType.TO, InternetAddress(forgetData.email))
                message.subject = Constants.SUBJECT
                message.setText("${Constants.MESSAGE}?token=$token")

                Transport.send(message)
            } catch (mex: MessagingException) {
                mex.printStackTrace()
            }

            BaseOkResponse()
        }, gson::toJson)
    }

    private fun initResetPassword() {
        post("/reset", { request, response ->
            val resetData = request.body().fromJson<ResetData>()

            val token = request.headers("token")
            if (token == null) {
                TokenNotFoundErrorResponse.halt(gson)
            }

            if (!tokenManager.validateAuthToken(token) || token.isEmpty()) {
                TokenNotFoundErrorResponse.halt(gson)
            }

            val user = userManager.getUserByToken(token)!!.toHibUser()
            val password = String(Hex.encodeHex(DigestUtils.md5(resetData.password)))

            user.password = password
            user.restored = false
            userManager.saveUser(user)
            BaseOkResponse()
        }, gson::toJson)
    }
}