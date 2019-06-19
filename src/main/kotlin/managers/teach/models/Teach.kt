package managers.teach.models

import com.google.gson.annotations.Expose
import managers.user.models.User

data class Teach(
    @Expose
    val teacher: User,
    @Expose
    val students: List<User>
)