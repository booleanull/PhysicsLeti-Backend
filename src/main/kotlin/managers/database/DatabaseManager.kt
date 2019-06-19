package managers.database

import controllers.auth.models.AuthData
import controllers.teach.models.TeachData
import controllers.teach.models.TeachIdData
import daggerApplicationComponent
import managers.database.base.toHibLabwork
import managers.database.base.toHibTeachNumber
import managers.database.dao.LabworkDao
import managers.database.dao.TeachDao
import managers.database.dao.UserDao
import managers.database.dao.models.HibLabwork
import managers.database.dao.models.HibTeach
import managers.database.dao.models.HibTeachNumber
import managers.database.dao.models.HibUser
import managers.labwork.models.*
import managers.teach.models.Teacher
import managers.user.models.User
import javax.inject.Inject

class DatabaseManager : IDatabaseManager {

    @Inject
    lateinit var userDao: UserDao
    @Inject
    lateinit var labworkDao: LabworkDao
    @Inject
    lateinit var teachDao: TeachDao

    init {
        daggerApplicationComponent.inject(this)
    }

    //region User Block
    override fun createUser(user: HibUser): Int {
        if (userDao.userWithDataExists(user)) {
            return -1
        }

        return userDao.create(user)
    }

    override fun getUser(id: Int): User? {
        return userDao.fetchById(id)?.toUser()
    }

    override fun getUser(authData: AuthData): User? {
        return userDao.findUser(authData)?.toUser()
    }

    override fun getUserByToken(token: String): User? {
        return userDao.findUser(token)?.toUser()
    }

    override fun getUserByEmail(email: String): User? {
        return userDao.findUserByEmail(email)?.toUser()
    }

    override fun saveUser(user: HibUser): User? {
        return userDao.update(user)?.toUser()
    }

    override fun removeUser(user: HibUser) {
        userDao.delete(user)
    }

    override fun getListOfNotConfirmed(): List<User> {
        return userDao.getListOfNotConfirmed()
    }

    //endregion
    //region Labwork Block
    override fun getListLabwork(): List<LabworkThemeList> {
        val labcodes = mutableListOf<LabworkThemeList>()
        val labs = labworkDao.fetchAll().map { it.toLabwork() }
        labs.forEach { lab ->
            var check = false
            labcodes.forEach { code ->
                if (lab.theme == code.theme) {
                    code.list.add(lab)
                    check = true
                }
            }
            if (!check) {
                labcodes.add(LabworkThemeList(lab.theme, mutableListOf(lab)))
            }
        }
        return labcodes
    }

    override fun createLabwork(labwork: HibLabwork) {
        labworkDao.create(labwork)
    }

    override fun getLabwork(id: Int): Labwork? {
        return labworkDao.fetchById(id)?.toLabwork()
    }

    override fun saveLabwork(labwork: HibLabwork): Boolean {
        val lab = getLabwork(labwork.id)?.toHibLabwork() ?: return false
        lab.id = labwork.id
        lab.title = labwork.title
        lab.theme = labwork.theme
        lab.description = labwork.description
        lab.link = labwork.link
        lab.protocol = labwork.protocol
        labworkDao.update(lab)
        return true
    }

    override fun removeLabwork(labwork: HibLabwork) {
        labworkDao.delete(labwork)
    }

    override fun getLabworksForTeacher(user: User): LabworkOpenClose {
        val labworkOpenClose = LabworkOpenClose(mutableListOf(), mutableListOf())
        val groupNumbers = teachDao.fetchAll().filter { it.teacher == user.id }.map { it.groupNumber.map { it.number } }
        val gNumbers = mutableListOf<Int>()
        groupNumbers.forEach {
            it.forEach {
                gNumbers.add(it)
            }
        }
        if (gNumbers.isEmpty()) return labworkOpenClose

        val users = userDao.fetchAll().filter { gNumbers.contains(it.groupNumber) }.sortedBy { it.groupNumber }
        users.forEach { user ->
            user.labworks.forEach { labwork ->
                val lab = getLabwork(labwork.labId)!!
                if (labwork.mark.isEmpty()) {
                    if (labwork.answer.isNotEmpty()) {
                        labworkOpenClose.open.add(
                            MadeLabworkUser(
                                user.toUser().toUserPreview(),
                                LabworkUnion(
                                    labwork.id,
                                    labwork.labId,
                                    lab.title,
                                    lab.description,
                                    lab.theme,
                                    lab.link,
                                    labwork.protocol,
                                    labwork.answer,
                                    labwork.mark
                                )
                            )
                        )
                    }
                } else {
                    labworkOpenClose.close.add(
                        MadeLabworkUser(
                            user.toUser().toUserPreview(),
                            LabworkUnion(
                                labwork.id,
                                labwork.labId,
                                lab.title,
                                lab.description,
                                lab.theme,
                                lab.link,
                                labwork.protocol,
                                labwork.answer,
                                labwork.mark
                            )
                        )
                    )
                }
            }
        }

        return labworkOpenClose
    }

    override fun getUserWithLabToMark(id: Int, teacher: User): HibUser? {
        val groupNumbers =
            teachDao.fetchAll().filter { it.teacher == teacher.id }.map { it.groupNumber.map { it.number } }.first()

        val users = userDao.fetchAll()
        val usersInGroup = users.filter { groupNumbers.contains(it.groupNumber) }
        usersInGroup.forEach { hibUser ->
            hibUser.labworks.forEach {
                if (it.id == id) {
                    return hibUser
                }
            }
        }
        return null
    }

    override fun getTeachers(): List<Teacher> {
        val teachers = mutableListOf<Teacher>()
        val teach = teachDao.fetchAll()
        val users = userDao.fetchAll().filter { it.type == 1 }
        users.forEach { user ->
            val groups = teach.firstOrNull { teacher -> teacher.teacher == user.id }
            if (groups != null) {
                teachers.add(
                    Teacher(
                        user.id,
                        user.firstName,
                        user.lastName,
                        user.email,
                        groups.groupNumber.map { it.number })
                )
            } else {
                teachers.add(Teacher(user.id, user.firstName, user.lastName, user.email, null))
            }
        }
        return teachers
    }

    //endregion
    //region Teach Block
    override fun createTeach(teachData: TeachData) {
        val teacher = teachDao.fetchAll().firstOrNull { it.teacher == teachData.id }
        if (teacher == null) {
            val hibTeach = HibTeach()
            hibTeach.teacher = teachData.id
            hibTeach.groupNumber = mutableSetOf(teachData.groupNumber.toHibTeachNumber())
            teachDao.create(hibTeach)
        } else {
            val hibTeachNumber = HibTeachNumber()
            hibTeachNumber.number = teachData.groupNumber
            if (!teacher.groupNumber.map { it.number }.contains(hibTeachNumber.number)) teacher.groupNumber.add(
                hibTeachNumber
            )
            teachDao.update(teacher)
        }
    }

    override fun deleteTeach(teach: TeachIdData) {
        val teacher = teachDao.fetchAll().firstOrNull { it.teacher == teach.id }
        teacher?.let {
            if (it.groupNumber.map { it.number }.contains(teach.groupNumber.toHibTeachNumber().number)) {
                it.groupNumber.removeIf { it.number == teach.groupNumber }
                teachDao.update(teacher)
            }
        }
    }
    //endregion
}