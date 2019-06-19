package controllers.teach.response.ok

import com.google.gson.annotations.Expose
import controllers.base.responses.BaseOkResponse
import managers.labwork.models.LabworkOpenClose

data class ListOfLabsForTeacherOkResponse(@Expose val labworks: LabworkOpenClose) : BaseOkResponse()