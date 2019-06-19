package controllers.settings

import com.google.gson.Gson
import controllers.base.BaseController
import daggerApplicationComponent
import managers.token.ITokenManager
import managers.user.IUserManager
import spark.Spark
import javax.inject.Inject

class SettingsController : BaseController {

    @Inject
    lateinit var gson: Gson
    @Inject
    lateinit var tokenManager: ITokenManager
    @Inject
    lateinit var userManager: IUserManager

    init {
        daggerApplicationComponent.inject(this)
    }

    override fun start() {
        initOptions()
        initBeforeMethod()
    }

    private fun initOptions() {
        Spark.options("/*") { request, response ->

            val accessControlRequestHeaders = request.headers("Access-Control-Request-Headers")
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders)
            }

            val accessControlRequestMethod = request.headers("Access-Control-Request-Method")
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod)
            }

            "OK"
        }
    }

    private fun initBeforeMethod() {
        Spark.before("*") { req, res ->
            res.header("Access-Control-Allow-Origin", "*")
        }
    }
}