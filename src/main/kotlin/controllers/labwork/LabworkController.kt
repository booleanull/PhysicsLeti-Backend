package controllers.labwork

import com.google.gson.Gson
import controllers.base.BaseController
import controllers.base.post
import controllers.base.responses.BaseOkResponse
import controllers.base.responses.error.TokenNotFoundErrorResponse
import controllers.base.responses.error.UserIsNotAdminErrorResponse
import controllers.base.responses.error.UserIsRestoredErrorResponse
import controllers.labwork.models.LabworkData
import controllers.labwork.models.LabworkIdData
import controllers.labwork.responses.error.LabworkWithThisIdNotFoundErrorResponse
import controllers.labwork.responses.ok.LabworkListOkResponse
import daggerApplicationComponent
import managers.database.base.toHibLabwork
import managers.labwork.ILabworkManager
import managers.labwork.models.Labwork
import managers.token.ITokenManager
import managers.user.IUserManager
import utils.fromJson
import javax.inject.Inject

class LabworkController : BaseController {

    @Inject
    lateinit var gson: Gson
    @Inject
    lateinit var userManager: IUserManager
    @Inject
    lateinit var labworkManager: ILabworkManager
    @Inject
    lateinit var tokenManager: ITokenManager

    init {
        daggerApplicationComponent.inject(this)
    }

    override fun start() {
        initGetListLabwork()
        initCreateLabwork()
        initDeleteLabwork()
        initUpdateLabwork()
    }

    private fun initGetListLabwork() {
        post("/api/lab", { request, response ->
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

            val list = labworkManager.getListLabwork()
            LabworkListOkResponse(list)
        }, gson::toJson)
    }

    private fun initCreateLabwork() {
        post("/api/lab/create", { request, response ->
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

            val labworkData = request.body().fromJson<LabworkData>()

            val token = request.headers("token")
            val tokenUser = userManager.getUserByToken(token)!!
            if (tokenUser.type != 2) {
                UserIsNotAdminErrorResponse.halt(gson)
            }

            labworkManager.createLabwork(labworkData.toLabwork().toHibLabwork())

            BaseOkResponse()
        }, gson::toJson)
    }

    private fun initDeleteLabwork() {
        post("/api/lab/delete", { request, response ->
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

            val labworkIdData = request.body().fromJson<LabworkIdData>()

            val token = request.headers("token")
            val tokenUser = userManager.getUserByToken(token)!!
            if (tokenUser.type != 2) {
                UserIsNotAdminErrorResponse.halt(gson)
            }

            val labwork = labworkManager.getLabwork(labworkIdData.id)
            if (labwork == null) {
                LabworkWithThisIdNotFoundErrorResponse.halt(gson)
            }

            val hibLabwork = labwork!!.toHibLabwork()
            labworkManager.removeLabwork(hibLabwork)

            BaseOkResponse()
        }, gson::toJson)
    }

    private fun initUpdateLabwork() {
        post("/api/lab/update", { request, response ->
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

            val labworkData = request.body().fromJson<Labwork>()

            val token = request.headers("token")
            val tokenUser = userManager.getUserByToken(token)!!
            if (tokenUser.type != 2) {
                UserIsNotAdminErrorResponse.halt(gson)
            }

            if (!labworkManager.saveLabwork(labworkData.toHibLabwork())) {
                LabworkWithThisIdNotFoundErrorResponse.halt(gson)
            }

            BaseOkResponse()
        }, gson::toJson)
    }
}