package controllers.labwork.responses.error

import controllers.base.responses.error.BaseErrorResponse

object LabworkWithThisIdNotFoundErrorResponse : BaseErrorResponse("Labwork with this id not found!", 408)