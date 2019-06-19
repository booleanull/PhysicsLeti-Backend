package managers.labwork.models

import com.google.gson.annotations.Expose

data class MadeLabworkLabSort(
    @Expose
    val type: Int,
    @Expose
    val labworks: MutableList<LabworkUnion>
)