package controllers.auth.models

import managers.user.models.User

data class RegisterData(
    val email: String,
    val firstName: String,
    val lastName: String,
    val type: Int,
    val groupNumber: Int = 0,
    val password: String
) {

    fun toUser() = User(0, "", false, false, email, password, firstName, lastName, type, groupNumber, emptyList())
}