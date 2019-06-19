package managers.labwork.models

import com.google.gson.annotations.Expose

data class LabworkUnion(
    @Expose
    val id: Int,
    @Expose
    val labId: Int,
    @Expose
    val title: String,
    @Expose
    val description: String,
    @Expose
    val theme: String,
    @Expose
    val link: String,
    @Expose
    val protocol: String,
    @Expose
    val answer: String,
    @Expose
    val mark: String
)