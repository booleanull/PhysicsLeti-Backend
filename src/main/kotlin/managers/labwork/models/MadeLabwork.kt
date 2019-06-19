package managers.labwork.models

import com.google.gson.annotations.Expose

data class MadeLabwork(
    @Expose
    val id: Int,
    @Expose
    val labId: Int,
    @Expose
    val protocol: String,
    @Expose
    val answer: String,
    @Expose
    val mark: String
)