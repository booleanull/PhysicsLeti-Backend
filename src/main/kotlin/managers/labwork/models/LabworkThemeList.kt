package managers.labwork.models

import com.google.gson.annotations.Expose

data class LabworkThemeList(
    @Expose
    val theme: String,
    @Expose
    val list: MutableList<Labwork>
)