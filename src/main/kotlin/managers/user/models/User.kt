package managers.user.models

import com.google.gson.annotations.Expose
import controllers.base.models.UserPreview
import managers.labwork.models.MadeLabwork

data class User(
    @Expose
    val id: Int,
    @Expose(serialize = false)
    val token: String,
    @Expose(serialize = false)
    val approve: Boolean = false,
    @Expose(serialize = false)
    val restored: Boolean = false,
    @Expose
    val email: String,
    @Expose(serialize = false)
    val password: String,
    @Expose
    val firstName: String,
    @Expose
    val lastName: String,
    @Expose
    val type: Int,
    @Expose
    val groupNumber: Int?,
    @Expose
    val labworks: List<MadeLabwork>
) {

    fun toUserPreview() = UserPreview(this.id, this.email, this.firstName, this.lastName, this.type, this.groupNumber)
}