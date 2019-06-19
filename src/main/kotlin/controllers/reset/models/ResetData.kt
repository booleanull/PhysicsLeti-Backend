package controllers.auth.models

import com.google.gson.annotations.Expose

data class ResetData(
    @Expose
    val password: String
)