package controllers.labwork.models

import managers.labwork.models.Labwork

data class LabworkData(
    val title: String,
    val description: String,
    val theme: String,
    val protocol: String,
    val link: String
) {

    fun toLabwork() = Labwork(0, title, description, theme, protocol, link)
}