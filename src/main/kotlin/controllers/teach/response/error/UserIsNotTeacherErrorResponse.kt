package controllers.teach.response.error

import controllers.base.responses.error.BaseErrorResponse

object UserIsNotTeacherErrorResponse : BaseErrorResponse("User is not teacher!", 415)