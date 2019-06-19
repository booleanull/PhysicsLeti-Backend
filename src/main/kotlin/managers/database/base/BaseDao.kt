package managers.database.base

import org.hibernate.Session
import org.hibernate.SessionFactory
import javax.persistence.EntityManager

/**
 * Abstract realisation of simple hibernate dao with few help-methods
 */
abstract class BaseDao<T : HibEntity>(
    val sessionFactory: SessionFactory,
    val type: Class<T>
) {

    open fun create(model: T): Int {
        return withSession({ it.save(model) as Int }) ?: -1
    }

    open fun fetchAll(): List<T> {
        return withEntityManager({
            val className = type.simpleName

            it.createQuery("FROM $className ", type)
                .resultList
                .toList()
        }) ?: listOf()
    }

    open fun fetchById(id: Int): T? {
        return withSession({
            it.get(type, id)
        })
    }

    open fun update(model: T): T? {
        return withSession({
            it.update(model)
            model
        })
    }

    open fun delete(model: T): Boolean {
        return withSession({
            it.delete(model)
            true
        }) ?: false
    }


    fun <T> withSession(
        action: (session: Session) -> T,
        onError: (() -> T)? = null
    ): T? {
        var session: Session? = null

        return try {
            session = sessionFactory.openSession()
            val transaction = session.beginTransaction()

            val result = action.invoke(session)

            transaction.commit()

            result
        } catch (e: Exception) {
            onError?.invoke()
        } finally {
            session?.close()
        }
    }

    fun <T> withEntityManager(
        action: (em: EntityManager) -> T,
        onError: (() -> T)? = null
    ): T? {
        var em: EntityManager? = null

        return try {
            em = sessionFactory.createEntityManager()
            em.transaction.begin()

            val result = action.invoke(em)

            em.transaction.commit()

            result
        } catch (e: Exception) {
            onError?.invoke()
        } finally {
            em?.close()
        }
    }
}