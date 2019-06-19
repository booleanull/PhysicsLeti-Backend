package controllers.madelabwork.models

import managers.labwork.models.MadeLabwork

data class MadeLabworkProtocolData(val id: Int, var protocol: String) {

    fun toMadeLabwork() = MadeLabwork(0, id, protocol, "", "")
}