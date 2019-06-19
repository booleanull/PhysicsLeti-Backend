package managers.teach

import controllers.teach.models.TeachData
import controllers.teach.models.TeachIdData
import managers.teach.models.Teacher

interface ITeachManager {

    fun createTeach(teachData: TeachData)

    fun deleteTeach(id: TeachIdData)

    fun getTeachers(): List<Teacher>
}