package managers.labwork

import daggerApplicationComponent
import managers.database.IDatabaseManager
import managers.database.dao.models.HibLabwork
import managers.database.dao.models.HibUser
import managers.labwork.models.Labwork
import managers.labwork.models.LabworkOpenClose
import managers.labwork.models.LabworkThemeList
import managers.user.models.User
import javax.inject.Inject

class LabworkManager : ILabworkManager {

    @Inject
    lateinit var databaseManager: IDatabaseManager

    init {
        daggerApplicationComponent.inject(this)
    }

    override fun getListLabwork(): List<LabworkThemeList> {
        return databaseManager.getListLabwork()
    }

    override fun createLabwork(labwork: HibLabwork) {
        databaseManager.createLabwork(labwork)
    }

    override fun getLabwork(id: Int): Labwork? {
        return databaseManager.getLabwork(id)
    }

    override fun saveLabwork(labwork: HibLabwork): Boolean {
        return databaseManager.saveLabwork(labwork)
    }

    override fun removeLabwork(labwork: HibLabwork) {
        databaseManager.removeLabwork(labwork)
    }

    override fun getLabworksForTeacher(user: User): LabworkOpenClose {
        return databaseManager.getLabworksForTeacher(user)
    }

    override fun getUserWithLabToMark(id: Int, teacher: User): HibUser? {
        return databaseManager.getUserWithLabToMark(id, teacher)
    }
}