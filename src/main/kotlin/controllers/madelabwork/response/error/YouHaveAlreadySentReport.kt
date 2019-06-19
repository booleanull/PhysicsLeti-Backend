package controllers.madelabwork.response.error

import controllers.base.responses.error.BaseErrorResponse

object YouHaveAlreadySentReport : BaseErrorResponse("You have already sent a report!", 408)