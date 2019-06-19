package managers.labwork.models

import com.google.gson.annotations.Expose
import controllers.base.models.UserPreview

data class MadeLabworkUser(
    @Expose
    val user: UserPreview,
    @Expose
    val labwork: LabworkUnion
)