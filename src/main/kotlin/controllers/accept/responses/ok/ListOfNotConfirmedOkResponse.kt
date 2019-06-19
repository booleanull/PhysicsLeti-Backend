package controllers.accept.responses.ok

import com.google.gson.annotations.Expose
import controllers.base.models.UserPreview
import controllers.base.responses.BaseOkResponse

class ListOfNotConfirmedOkResponse(@Expose val users: List<UserPreview>) : BaseOkResponse()