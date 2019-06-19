package di

import dagger.Module
import dagger.Provides
import managers.database.dao.LabworkDao
import managers.database.dao.TeachDao
import managers.database.dao.UserDao
import org.hibernate.SessionFactory
import org.hibernate.cfg.Configuration
import javax.inject.Singleton

@Module
open class DaoModules {

    @Provides
    @Singleton
    internal fun provideSessionFactory(): SessionFactory {
        return Configuration()
            .configure()
            .buildSessionFactory()
    }

    @Provides
    @Singleton
    internal fun provideUserDao(sessionFactory: SessionFactory): UserDao {
        return UserDao(sessionFactory)
    }

    @Provides
    @Singleton
    internal fun provideLabworkDao(sessionFactory: SessionFactory): LabworkDao {
        return LabworkDao(sessionFactory)
    }

    @Provides
    @Singleton
    internal fun provideTeachDao(sessionFactory: SessionFactory): TeachDao {
        return TeachDao(sessionFactory)
    }
}