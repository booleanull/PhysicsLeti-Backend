package managers.database

import controllers.auth.models.AuthData
import controllers.teach.models.TeachData
import controllers.teach.models.TeachIdData
import managers.database.dao.models.HibLabwork
import managers.database.dao.models.HibUser
import managers.labwork.models.Labwork
import managers.labwork.models.LabworkOpenClose
import managers.labwork.models.LabworkThemeList
import managers.teach.models.Teacher
import managers.user.models.User

interface IDatabaseManager {

    fun createUser(user: HibUser): Int

    fun getUser(id: Int): User?

    fun getUser(authData: AuthData): User?

    fun getUserByToken(token: String): User?

    fun getUserByEmail(email: String): User?

    fun saveUser(user: HibUser): User?

    fun removeUser(user: HibUser)

    fun getListOfNotConfirmed(): List<User>

    fun getListLabwork(): List<LabworkThemeList>

    fun createLabwork(labwork: HibLabwork)

    fun getLabwork(id: Int): Labwork?

    fun saveLabwork(labwork: HibLabwork): Boolean

    fun removeLabwork(labwork: HibLabwork)

    fun getLabworksForTeacher(user: User): LabworkOpenClose

    fun getUserWithLabToMark(id: Int, teacher: User): HibUser?

    fun createTeach(teachData: TeachData)

    fun deleteTeach(id: TeachIdData)

    fun getTeachers(): List<Teacher>
}