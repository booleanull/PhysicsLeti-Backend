package managers.labwork.models

import com.google.gson.annotations.Expose

data class LabworkOpenClose(
    @Expose
    var open: MutableList<MadeLabworkUser>,
    @Expose
    var close: MutableList<MadeLabworkUser>
)