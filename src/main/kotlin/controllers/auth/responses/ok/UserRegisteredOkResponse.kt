package controllers.auth.responses.ok

import com.google.gson.annotations.Expose
import controllers.base.responses.BaseOkResponse

class UserRegisteredOkResponse(
    @Expose
    val name: String,
    @Expose
    val type: Int,
    @Expose
    val token: String
) : BaseOkResponse()