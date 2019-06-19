package managers.database.base

import managers.database.dao.models.*
import managers.labwork.models.Labwork
import managers.labwork.models.MadeLabwork
import managers.teach.models.TeachGroup
import managers.user.models.User

fun User.toHibUser() = HibUser().apply {
    id = this@toHibUser.id
    token = this@toHibUser.token
    approve = this@toHibUser.approve
    restored = this@toHibUser.restored
    email = this@toHibUser.email
    password = this@toHibUser.password
    firstName = this@toHibUser.firstName
    lastName = this@toHibUser.lastName
    type = this@toHibUser.type
    groupNumber = this@toHibUser.groupNumber
    labworks = this@toHibUser.labworks.map { it.toHibMadeLabworks() }.toMutableList()
}

fun Labwork.toHibLabwork() = HibLabwork().apply {
    id = this@toHibLabwork.id
    title = this@toHibLabwork.title
    description = this@toHibLabwork.description
    theme = this@toHibLabwork.theme
    protocol = this@toHibLabwork.protocol
    link = this@toHibLabwork.link
}

fun MadeLabwork.toHibMadeLabworks() = HibMadeLabwork().apply {
    id = this@toHibMadeLabworks.id
    labId = this@toHibMadeLabworks.labId
    protocol = this@toHibMadeLabworks.protocol
    answer = this@toHibMadeLabworks.answer
    mark = this@toHibMadeLabworks.mark
}

fun TeachGroup.toHibTeach() = HibTeach().apply {
    teacher = this@toHibTeach.id
    groupNumber = this@toHibTeach.groupNumber.map { it.toHibTeachNumber() }.toMutableSet()
}

fun Int.toHibTeachNumber() = HibTeachNumber().apply {
    number = this@toHibTeachNumber
}