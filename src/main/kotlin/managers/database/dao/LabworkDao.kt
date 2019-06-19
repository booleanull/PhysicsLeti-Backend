package managers.database.dao

import managers.database.base.BaseDao
import managers.database.dao.models.HibLabwork
import org.hibernate.SessionFactory

class LabworkDao(sessionFactory: SessionFactory) : BaseDao<HibLabwork>(sessionFactory, HibLabwork::class.java)