package managers.teach.models

import com.google.gson.annotations.Expose

data class Teacher(
    @Expose
    val id: Int,
    @Expose
    val firstName: String,
    @Expose
    val lastName: String,
    @Expose
    val email: String,
    @Expose
    val groups: List<Int>?
)