package managers.teach

import controllers.teach.models.TeachData
import controllers.teach.models.TeachIdData
import daggerApplicationComponent
import managers.database.IDatabaseManager
import managers.teach.models.Teacher
import javax.inject.Inject

class TeachManager : ITeachManager {

    @Inject
    lateinit var databaseManager: IDatabaseManager

    init {
        daggerApplicationComponent.inject(this)
    }

    override fun createTeach(teachData: TeachData) {
        databaseManager.createTeach(teachData)
    }

    override fun deleteTeach(id: TeachIdData) {
        databaseManager.deleteTeach(id)
    }

    override fun getTeachers(): List<Teacher> {
        return databaseManager.getTeachers()
    }
}