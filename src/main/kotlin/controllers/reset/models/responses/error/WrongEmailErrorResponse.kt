package controllers.reset.models.responses.error

import controllers.base.responses.error.BaseErrorResponse

object WrongEmailErrorResponse : BaseErrorResponse("Wrong email!", 406)