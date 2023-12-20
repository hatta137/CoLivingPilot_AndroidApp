package de.fhe.ai.colivingpilot.di

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import de.fhe.ai.colivingpilot.core.CoLiPiApplication
import de.fhe.ai.colivingpilot.storage.Repository
import de.fhe.ai.colivingpilot.storage.ShoppingListItemDao
import de.fhe.ai.colivingpilot.storage.TaskAssignedUserDao
import de.fhe.ai.colivingpilot.storage.TaskDao
import de.fhe.ai.colivingpilot.storage.UserDao
import de.fhe.ai.colivingpilot.storage.WgDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

/*
@Module
@InstallIn(SingletonComponent::class)
class AppModule() {

    @Provides
    @Singleton
    fun provideDatabase(app: Application,
                        callback: WgDatabase.Callback
    ) : WgDatabase {
        val db =
        Room.databaseBuilder(app, WgDatabase::class.java, "wg_database")
            .fallbackToDestructiveMigration()
            .addCallback(callback)
            .build()
        Log.d(CoLiPiApplication.LOG_TAG, "Database provided.")
        return db
    }

    @Provides
    fun provideTaskDao(db: WgDatabase) = db.taskDao()

    @Provides
    fun provideShoppingListItemDao(db: WgDatabase) = db.shoppingListItemDao()

    @Provides
    fun provideTaskAssignedUserDao(db: WgDatabase) = db.taskAssignedUserDao()

    @Provides
    fun provideUserDao(db: WgDatabase) = db.userDao()

    @Provides
    @Singleton
    fun provideRepository(
        userDao: UserDao,
        taskDao: TaskDao,
        shoppingListItemDao: ShoppingListItemDao,
        taskAssignedUserDao: TaskAssignedUserDao,

    ) = Repository(userDao, taskDao, shoppingListItemDao, taskAssignedUserDao)

    @Provides
    @Singleton
    fun provideApplicationScope() = CoroutineScope(SupervisorJob())
}

 */