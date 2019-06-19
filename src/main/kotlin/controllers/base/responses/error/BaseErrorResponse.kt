package controllers.base.responses.error

import controllers.base.responses.BaseResponse

open class BaseErrorResponse(error: String, httpStatus: Int) : BaseResponse("error", error, httpStatus = httpStatus)