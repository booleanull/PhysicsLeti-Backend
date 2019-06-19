package managers.labwork.models

import com.google.gson.annotations.Expose

data class Labwork(
    @Expose
    val id: Int,
    @Expose
    val title: String,
    @Expose
    val description: String,
    @Expose(serialize = false)
    val theme: String,
    @Expose
    val protocol: String,
    @Expose
    val link: String
)