package controllers.auth.responses.error

import controllers.base.responses.error.BaseErrorResponse

object WrongUserDataErrorResponse : BaseErrorResponse("Wrong auth data!", 402)