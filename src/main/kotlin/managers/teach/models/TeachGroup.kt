package managers.teach.models

import com.google.gson.annotations.Expose

data class TeachGroup(
    @Expose
    val id: Int,
    @Expose
    val groupNumber: Set<Int>
)