package controllers.base.models

import com.google.gson.annotations.Expose

data class UserPreview(
    @Expose
    val id: Int,
    @Expose
    val email: String,
    @Expose
    val firstName: String,
    @Expose
    val lastName: String,
    @Expose
    val type: Int,
    @Expose
    val groupNumber: Int?
)