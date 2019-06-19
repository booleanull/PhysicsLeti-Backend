package controllers.base

import com.google.gson.JsonParseException
import controllers.base.responses.BaseResponse
import controllers.base.responses.error.BadRequestErrorResponse
import spark.Request
import spark.Response
import spark.Spark

private val routeWrapper: (handler: (request: Request, response: Response) -> BaseResponse, request: Request, response: Response) -> Any =
    { handler, request, response ->
        val resp = try {
            handler.invoke(request, response)
        } catch (e: JsonParseException) {
            BadRequestErrorResponse
        }

        resp
    }

fun post(path: String, route: (Request, Response) -> BaseResponse, transformer: (model: Any) -> String) {
    Spark.post(
        path,
        { request: Request, response: Response -> routeWrapper.invoke(route, request, response) },
        transformer
    )
}