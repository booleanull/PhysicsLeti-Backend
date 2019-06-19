package controllers.teach

import com.google.gson.Gson
import controllers.base.BaseController
import controllers.base.post
import controllers.base.responses.BaseOkResponse
import controllers.base.responses.error.TokenNotFoundErrorResponse
import controllers.base.responses.error.UserIsNotAdminErrorResponse
import controllers.base.responses.error.UserIsRestoredErrorResponse
import controllers.base.responses.error.UserWithThisIdNotFoundErrorResponse
import controllers.labwork.responses.error.LabworkWithThisIdNotFoundErrorResponse
import controllers.madelabwork.models.MadeLabworkMarkData
import controllers.teach.models.TeachData
import controllers.teach.models.TeachIdData
import controllers.teach.response.error.UserIsNotTeacherErrorResponse
import controllers.teach.response.ok.ListOfLabsForTeacherOkResponse
import controllers.teach.response.ok.ListOfTeacherOkResponse
import daggerApplicationComponent
import managers.labwork.ILabworkManager
import managers.teach.ITeachManager
import managers.token.ITokenManager
import managers.user.IUserManager
import utils.fromJson
import javax.inject.Inject

class TeachController : BaseController {

    @Inject
    lateinit var gson: Gson
    @Inject
    lateinit var userManager: IUserManager
    @Inject
    lateinit var teachManager: ITeachManager
    @Inject
    lateinit var labworkManager: ILabworkManager
    @Inject
    lateinit var tokenManager: ITokenManager

    init {
        daggerApplicationComponent.inject(this)
    }

    override fun start() {
        initSetGroupToTeacher()
        initDeleteTeacher()
        initGetLabworks()
        initSetMarkLabwork()
        initListOfTeacher()
    }


    private fun initListOfTeacher() {
        post("/api/teachers", { request, response ->
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

            val teachers = teachManager.getTeachers()
            ListOfTeacherOkResponse(teachers)
        }, gson::toJson)
    }

    private fun initSetGroupToTeacher() {
        post("/api/teachers/set", { request, response ->
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

            val teachData = request.body().fromJson<TeachData>()

            val token = request.headers("token")
            val tokenUser = userManager.getUserByToken(token)!!
            if (tokenUser.type != 2) {
                UserIsNotAdminErrorResponse.halt(gson)
            }

            val user = userManager.getUser(teachData.id)
            if (user == null) {
                UserWithThisIdNotFoundErrorResponse.halt(gson)
            }
            if (user!!.type != 1) {
                UserIsNotTeacherErrorResponse.halt(gson)
            }

            teachManager.createTeach(teachData)

            BaseOkResponse()
        }, gson::toJson)
    }

    private fun initDeleteTeacher() {
        post("/api/teachers/delete", { request, response ->
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

            val teachIdData = request.body().fromJson<TeachIdData>()

            val token = request.headers("token")
            val tokenUser = userManager.getUserByToken(token)!!
            if (tokenUser.type != 2) {
                UserIsNotAdminErrorResponse.halt(gson)
            }

            val user = userManager.getUser(teachIdData.id)
            if (user == null) {
                UserWithThisIdNotFoundErrorResponse.halt(gson)
            }
            if (user!!.type != 1) {
                UserIsNotTeacherErrorResponse.halt(gson)
            }

            teachManager.deleteTeach(teachIdData)

            BaseOkResponse()
        }, gson::toJson)
    }

    private fun initGetLabworks() {
        post("/api/teach", { request, response ->
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
            if (tokenUser.type != 1) {
                UserIsNotTeacherErrorResponse.halt(gson)
            }

            ListOfLabsForTeacherOkResponse(labworkManager.getLabworksForTeacher(tokenUser))
        }, gson::toJson)
    }

    private fun initSetMarkLabwork() {
        post("/api/teach/mark", { request, response ->
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

            val data = request.body().fromJson<MadeLabworkMarkData>()

            val token = request.headers("token")
            val tokenUser = userManager.getUserByToken(token)!!
            if (tokenUser.type != 1) {
                UserIsNotTeacherErrorResponse.halt(gson)
            }

            val user = labworkManager.getUserWithLabToMark(data.id, tokenUser)
            if (user == null) {
                LabworkWithThisIdNotFoundErrorResponse.halt(gson)
            }
            user!!.labworks.forEach {
                if (it.id == data.id) {
                    it.mark = data.mark
                }
            }
            userManager.saveUser(user)

            BaseOkResponse()
        }, gson::toJson)
    }
}