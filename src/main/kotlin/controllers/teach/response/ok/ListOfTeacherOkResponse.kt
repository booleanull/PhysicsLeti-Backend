package controllers.teach.response.ok

import com.google.gson.annotations.Expose
import controllers.base.responses.BaseOkResponse
import managers.teach.models.Teacher

data class ListOfTeacherOkResponse(@Expose val teachers: List<Teacher>) : BaseOkResponse()