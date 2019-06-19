package managers.labwork

import managers.database.dao.models.HibLabwork
import managers.database.dao.models.HibUser
import managers.labwork.models.Labwork
import managers.labwork.models.LabworkOpenClose
import managers.labwork.models.LabworkThemeList
import managers.user.models.User

interface ILabworkManager {

    /**
     * Method to return all [Labwork]
     *
     * @return List of [Labwork]
     */
    fun getListLabwork(): List<LabworkThemeList>

    /**
     * Method creates [labwork] to database
     */
    fun createLabwork(labwork: HibLabwork)

    /**
     * Method to get [Labwork] by [id]
     *
     * @return [Labwork]
     */
    fun getLabwork(id: Int): Labwork?

    /**
     * Method save [labwork] to database
     */
    fun saveLabwork(labwork: HibLabwork): Boolean

    /**
     * Method delete [labwork] to database
     */
    fun removeLabwork(labwork: HibLabwork)

    fun getLabworksForTeacher(user: User): LabworkOpenClose

    fun getUserWithLabToMark(id: Int, teacher: User): HibUser?
}