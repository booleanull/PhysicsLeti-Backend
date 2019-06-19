package controllers.base.responses

open class BaseOkResponse(httpStatus: Int = 200) : BaseResponse("ok", httpStatus = httpStatus)