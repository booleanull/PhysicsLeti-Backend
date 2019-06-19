package controllers.accept

import com.google.gson.Gson
import controllers.accept.responses.error.UserIsAlreadyAcceptedErrorResponse
import controllers.accept.responses.ok.ListOfNotConfirmedOkResponse
import controllers.base.BaseController
import controllers.base.models.IdData
import controllers.base.post
import controllers.base.responses.BaseOkResponse
import controllers.base.responses.error.TokenNotFoundErrorResponse
import controllers.base.responses.error.UserIsNotAdminErrorResponse
import controllers.base.responses.error.UserIsRestoredErrorResponse
import controllers.base.responses.error.UserWithThisIdNotFoundErrorResponse
import daggerApplicationComponent
import managers.database.base.toHibUser
import managers.token.ITokenManager
import managers.user.IUserManager
import utils.Constants
import utils.fromJson
import javax.inject.Inject
import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class AcceptController : BaseController {

    @Inject
    lateinit var gson: Gson
    @Inject
    lateinit var userManager: IUserManager
    @Inject
    lateinit var emailSession: Session
    @Inject
    lateinit var tokenManager: ITokenManager

    init {
        daggerApplicationComponent.inject(this)
    }

    override fun start() {
        initListOfNotConfirmed()
        initAcceptUser()
        initRejectUser()
    }

    private fun initListOfNotConfirmed() {
        post("/api/confirm", { request, response ->
            val beforeToken = request.headers("token") ?: throw TokenNotFoundErrorResponse.halt(gson)

            if (!tokenManager.validateAuthToken(beforeToken) || beforeToken.isEmpty()) {
                throw TokenNotFoundErrorResponse.halt(gson)
            }

            val beforeUser = userManager.getUserByToken(beforeToken)!!
            if (beforeUser.restored) {
                throw UserIsRestoredErrorResponse.halt(gson)
            }
            if (!beforeUser.approve) {
                throw UserIsRestoredErrorResponse.halt(gson)
            }

            val token = request.headers("token")
            val tokenUser = userManager.getUserByToken(token)!!

            if (tokenUser.type != 2) {
                UserIsNotAdminErrorResponse.halt(gson)
            }

            val users = userManager.getListOfNotConfirmed()
            val usersConfirm = users.map { it.toUserPreview() }
            ListOfNotConfirmedOkResponse(usersConfirm)
        }, gson::toJson)
    }

    private fun initAcceptUser() {
        post("/api/accept", { request, response ->
            val beforeToken = request.headers("token") ?: throw TokenNotFoundErrorResponse.halt(gson)

            if (!tokenManager.validateAuthToken(beforeToken) || beforeToken.isEmpty()) {
                throw TokenNotFoundErrorResponse.halt(gson)
            }

            val beforeUser = userManager.getUserByToken(beforeToken)!!
            if (beforeUser.restored) {
                throw UserIsRestoredErrorResponse.halt(gson)
            }
            if (!beforeUser.approve) {
                throw UserIsRestoredErrorResponse.halt(gson)
            }

            val userIdData = request.body().fromJson<IdData>()

            val token = request.headers("token")
            val tokenUser = userManager.getUserByToken(token)!!
            if (tokenUser.type != 2) {
                UserIsNotAdminErrorResponse.halt(gson)
            }

            val user = userManager.getUser(userIdData.id)
            if (user == null) UserWithThisIdNotFoundErrorResponse.halt(gson)

            if (user!!.approve) UserIsAlreadyAcceptedErrorResponse.halt(gson)

            val hibUser = user.toHibUser()
            hibUser.approve = true
            userManager.saveUser(hibUser)
            BaseOkResponse()
        }, gson::toJson)
    }

    private fun initRejectUser() {
        post("/api/reject", { request, response ->
            val beforeToken = request.headers("token") ?: throw TokenNotFoundErrorResponse.halt(gson)

            if (!tokenManager.validateAuthToken(beforeToken) || beforeToken.isEmpty()) {
                throw TokenNotFoundErrorResponse.halt(gson)
            }

            val beforeUser = userManager.getUserByToken(beforeToken)!!
            if (beforeUser.restored) {
                throw UserIsRestoredErrorResponse.halt(gson)
            }
            if (!beforeUser.approve) {
                throw UserIsRestoredErrorResponse.halt(gson)
            }

            val userIdData = request.body().fromJson<IdData>()

            val token = request.headers("token")
            val tokenUser = userManager.getUserByToken(token)!!
            if (tokenUser.type != 2) {
                UserIsNotAdminErrorResponse.halt(gson)
            }

            val user = userManager.getUser(userIdData.id)
            if (user == null) UserWithThisIdNotFoundErrorResponse.halt(gson)
            if (user!!.approve) UserIsAlreadyAcceptedErrorResponse.halt(gson)

            userManager.removeUser(user.toHibUser())

            try {
                val message = MimeMessage(emailSession)
                message.setFrom(InternetAddress(Constants.USER))
                message.addRecipient(Message.RecipientType.TO, InternetAddress(user.email))
                message.subject = Constants.SUBJECT_REJECT
                message.setText(Constants.MESSAGE_REJECT)

                Transport.send(message)
            } catch (mex: MessagingException) {
                mex.printStackTrace()
            }

            BaseOkResponse()
        }, gson::toJson)
    }
}