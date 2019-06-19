package controllers.labwork.responses.ok

import com.google.gson.annotations.Expose
import controllers.base.responses.BaseOkResponse
import managers.labwork.models.LabworkThemeList

class LabworkListOkResponse(@Expose val labworks: List<LabworkThemeList>) : BaseOkResponse()