package controllers.base.responses

import com.google.gson.Gson
import com.google.gson.annotations.Expose
import spark.HaltException
import spark.Spark
import utils.toJsonString

open class BaseResponse(
    @Expose
    val status: String,
    @Expose
    val error: String? = null,
    @Expose
    val httpStatus: Int
) {

    fun halt(gson: Gson): HaltException {
        return Spark.halt(this.httpStatus, this.toJsonString(gson))
    }
}