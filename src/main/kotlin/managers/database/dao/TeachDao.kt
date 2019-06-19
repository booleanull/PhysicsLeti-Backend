package managers.database.dao

import managers.database.base.BaseDao
import managers.database.dao.models.HibTeach
import org.hibernate.SessionFactory

class TeachDao(sessionFactory: SessionFactory) : BaseDao<HibTeach>(sessionFactory, HibTeach::class.java) {

    fun teachWithDataExists(id: Int): Int {
        return withEntityManager({
            it.createQuery(
                "FROM HibTeach " +
                        "WHERE teacher=:teacher", HibTeach::class.java
            )
                .setParameter("teacher", id)
                .resultList
                .firstOrNull()?.id
        }) ?: -1
    }

    fun findTeach(id: Int): HibTeach? {
        val result = withEntityManager({
            it.createQuery(
                "FROM HibTeach " +
                        "WHERE teacher=:teacher", HibTeach::class.java
            )
                .setParameter("teacher", id)
                .resultList
                .toList()
        }) ?: emptyList()

        return result.firstOrNull()
    }
}