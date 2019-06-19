package managers.database.dao

import controllers.auth.models.AuthData
import managers.database.base.BaseDao
import managers.database.dao.models.HibUser
import managers.user.models.User
import org.hibernate.SessionFactory

class UserDao(sessionFactory: SessionFactory) : BaseDao<HibUser>(sessionFactory, HibUser::class.java) {

    fun userWithDataExists(user: HibUser): Boolean {
        val email = user.email

        return withEntityManager({
            it.createQuery(
                "FROM HibUser " +
                        "WHERE email=:email", HibUser::class.java
            )
                .setParameter("email", email)
                .resultList
                .isNotEmpty()
        }) ?: true
    }

    fun findUser(token: String): HibUser? {
        val result = withEntityManager({
            it.createQuery(
                "FROM HibUser " +
                        "WHERE token=:token", HibUser::class.java
            )
                .setParameter("token", token)
                .resultList
                .toList()
        }) ?: emptyList()

        return result.firstOrNull()
    }

    fun findUser(authData: AuthData): HibUser? {
        val result = withEntityManager({
            it.createQuery(
                "FROM HibUser " +
                        "WHERE email=:email AND password=:password", HibUser::class.java
            )
                .setParameter("email", authData.email)
                .setParameter("password", authData.password)
                .resultList
                .toList()
        }) ?: emptyList()

        return result.firstOrNull()
    }

    fun findUserByEmail(email: String): HibUser? {
        val result = withEntityManager({
            it.createQuery(
                "FROM HibUser " +
                        "WHERE email=:email", HibUser::class.java
            )
                .setParameter("email", email)
                .resultList
                .toList()
        }) ?: emptyList()

        return result.firstOrNull()
    }

    fun getListOfNotConfirmed(): List<User> {
        val result = withEntityManager({
            it.createQuery(
                "FROM HibUser " +
                        "WHERE approve=false", HibUser::class.java
            )
                .resultList
                .toList()
        }) ?: emptyList()

        return result.map { it.toUser() }
    }
}