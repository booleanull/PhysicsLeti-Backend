package controllers.auth.models

import com.google.gson.annotations.Expose

data class ForgetData(
    @Expose
    val email: String
)