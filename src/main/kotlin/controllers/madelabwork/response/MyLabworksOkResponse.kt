package controllers.madelabwork.response

import com.google.gson.annotations.Expose
import controllers.base.responses.BaseOkResponse
import managers.labwork.models.MadeLabworkLabSort

class MyLabworksOkResponse(@Expose val types: List<MadeLabworkLabSort>) : BaseOkResponse()