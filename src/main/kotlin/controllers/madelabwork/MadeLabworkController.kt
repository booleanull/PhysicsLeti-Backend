package controllers.madelabwork

import com.google.gson.Gson
import controllers.base.BaseController
import controllers.base.post
import controllers.base.responses.BaseOkResponse
import controllers.base.responses.error.TokenNotFoundErrorResponse
import controllers.base.responses.error.UserIsNotStudentErrorResponse
import controllers.base.responses.error.UserIsRestoredErrorResponse
import controllers.labwork.responses.error.LabworkWithThisIdNotFoundErrorResponse
import controllers.madelabwork.models.AnswerData
import controllers.madelabwork.models.MadeLabworkProtocolData
import controllers.madelabwork.response.MyLabworksOkResponse
import controllers.madelabwork.response.error.YouHaveAlreadySentReport
import daggerApplicationComponent
import managers.database.base.toHibMadeLabworks
import managers.database.base.toHibUser
import managers.labwork.ILabworkManager
import managers.labwork.models.LabworkUnion
import managers.labwork.models.MadeLabworkLabSort
import managers.token.ITokenManager
import managers.user.IUserManager
import utils.fromJson
import javax.inject.Inject


class MadeLabworkController : BaseController {

    @Inject
    lateinit var gson: Gson
    @Inject
    lateinit var userManager: IUserManager
    @Inject
    lateinit var tokenManager: ITokenManager
    @Inject
    lateinit var labworkManager: ILabworkManager

    init {
        daggerApplicationComponent.inject(this)
    }

    override fun start() {
        initMyLabwork()
        initProtocolLabwork()
        initAnswerLabwork()
    }

    private fun initMyLabwork() {
        post("/api/lab/my", { request, response ->
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
            val user = userManager.getUserByToken(token)!!

            if (user.type != 0) {
                println()
                UserIsNotStudentErrorResponse.halt(gson)
            }

            val labs = mutableListOf<LabworkUnion>()
            val labsort = mutableListOf(
                MadeLabworkLabSort(0, mutableListOf()),
                MadeLabworkLabSort(1, mutableListOf()),
                MadeLabworkLabSort(2, mutableListOf())
            )
            val labworks = user.labworks
            labworks.forEach {
                val labwork = labworkManager.getLabwork(it.labId)
                labs.add(
                    LabworkUnion(
                        it.id,
                        it.labId,
                        labwork!!.title,
                        labwork.description,
                        labwork.theme,
                        labwork.link,
                        it.protocol,
                        it.answer,
                        it.mark
                    )
                )
            }
            labs.forEach {
                when {
                    it.answer.isEmpty() -> labsort[0].labworks.add(it)
                    it.mark.isEmpty() -> labsort[1].labworks.add(it)
                    else -> labsort[2].labworks.add(it)
                }
            }
            MyLabworksOkResponse(labsort.filter { it.labworks.isNotEmpty() })
        }, gson::toJson)
    }

    private fun initProtocolLabwork() {
        post("/api/lab/protocol", { request, response ->
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

            val madeLabworkData = request.body().fromJson<MadeLabworkProtocolData>()

            if (labworkManager.getLabwork(madeLabworkData.id) == null) {
                LabworkWithThisIdNotFoundErrorResponse.halt(gson)
            }

            val token = request.headers("token")
            val user = userManager.getUserByToken(token)!!
            val hibUser = user.toHibUser()

            if (hibUser.type != 0) {
                UserIsNotStudentErrorResponse.halt(gson)
            }

            hibUser.labworks.forEach {
                if (it.labId == madeLabworkData.id && it.mark == "") {
                    YouHaveAlreadySentReport.halt(gson)
                }
            }

            madeLabworkData.protocol = madeLabworkData.protocol.replace(" contenteditable=\"true\"", "")
                .replace(" contenteditable=\\\"true\\\"", "")

            val labworks = hibUser.labworks
            val madeLabwork = madeLabworkData.toMadeLabwork().toHibMadeLabworks()
            labworks.add(madeLabwork)
            userManager.saveUser(hibUser)
            BaseOkResponse()
        }, gson::toJson)
    }

    private fun initAnswerLabwork() {
        post("/api/lab/answer", { request, response ->
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
            val user = userManager.getUserByToken(token)!!
            val hibUser = user.toHibUser()

            val answerData = request.body().fromJson<AnswerData>()

            if (hibUser.type != 0) {
                UserIsNotStudentErrorResponse.halt(gson)
            }

            val labworks = hibUser.labworks
            var state = false
            labworks.forEach {
                if (it.id == answerData.id) {
                    it.answer = answerData.answer
                    state = true
                }
            }
            if (!state) LabworkWithThisIdNotFoundErrorResponse.halt(gson)

            hibUser.labworks = labworks
            userManager.saveUser(hibUser)

            return@post BaseOkResponse()
        }, gson::toJson)
    }
}